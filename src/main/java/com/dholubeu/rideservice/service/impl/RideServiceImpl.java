package com.dholubeu.rideservice.service.impl;

import com.dholubeu.rideservice.domain.Coordinate;
import com.dholubeu.rideservice.domain.Promocode;
import com.dholubeu.rideservice.domain.Ride;
import com.dholubeu.rideservice.domain.exception.ResourceDoesNotExistException;
import com.dholubeu.rideservice.kafka.KfProducer;
import com.dholubeu.rideservice.kafka.Message;
import com.dholubeu.rideservice.repository.RideRepository;
import com.dholubeu.rideservice.service.CoordinateService;
import com.dholubeu.rideservice.service.PromocodeService;
import com.dholubeu.rideservice.service.RideService;
import com.dholubeu.rideservice.service.client.DriverClient;
import com.dholubeu.rideservice.service.client.PassengerClient;
import com.dholubeu.rideservice.service.property.DemandProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    public static final String RESOURCE_DOES_NOT_EXIST_BY_ID_MESSAGE = "Ride with id %d does not exist";

    private final RideRepository rideRepository;
    private final PromocodeService promocodeService;
    private final CoordinateService coordinateService;
    private final DriverClient driverClient;
    private final PassengerClient passengerClient;
    private final KfProducer kfProducer;

    @Override
    public Ride create(Ride ride) {
        Message message = new Message();
        ride.setDateTime(LocalDateTime.now());
        ride.setDemandValue(calculateDemandValue(ride)
                .setScale(1, BigDecimal.ROUND_HALF_UP));
                ride.setDestination(calculateDestination(ride, message)
                        .setScale(1, BigDecimal.ROUND_HALF_UP));
        ride.setCost(calculateCost(ride)
                .setScale(1, BigDecimal.ROUND_HALF_UP));
        ride.setStatus(Ride.Status.NEW);
        ride = rideRepository.save(ride);
        message.setRideId(ride.getId());
        kfProducer.send(message);
        return ride;
    }

    @Override
    public Ride findById(Long id) {
        return rideRepository.findById(id).orElseThrow(
                () -> new ResourceDoesNotExistException(
                        String.format(RESOURCE_DOES_NOT_EXIST_BY_ID_MESSAGE, id)));
    }

    @Override
    public List<Ride> findAllByPassengerId(Long passengerId) {
        return rideRepository.findAllByPassengerId(passengerId);
    }

    @Override
    public List<Ride> findAllByDriverId(Long driverId) {
        return rideRepository.findAllByDriverId(driverId);
    }

    @Override
    public Ride updateStatus(Long id, Ride.Status status) {
        Ride ride = findById(id);
        ride.setStatus(status);
        return rideRepository.save(ride);
    }

    @Override
    public Ride setDriverId(Long id, Long driverId) {
        Ride ride = updateStatus(id, Ride.Status.ACCEPTED);
        ride.setDriverId(driverId);
        return rideRepository.save(ride);
    }

    @Override
    public Ride setPassengerRating(Long id, BigDecimal passengerRating) {
        Ride ride = findById(id);
        ride.setPassengerRating(passengerRating);
        List<Ride> rides = findAllByPassengerId(ride.getPassengerId());
        BigDecimal averageRating = rides.stream()
                .map(Ride::getPassengerRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(passengerRating)
                .divide(BigDecimal.valueOf(rides.size() + 1), 2, BigDecimal.ROUND_HALF_UP);
        passengerClient.updateRating(ride.getPassengerId(), averageRating);
        return rideRepository.save(ride);
    }

    @Override
    public Ride setDriverRating(Long id, BigDecimal driverRating) {
        Ride ride = findById(id);
        ride.setDriverRating(driverRating);
        List<Ride> rides = findAllByPassengerId(ride.getDriverId());
        BigDecimal averageRating = rides.stream()
                .map(Ride::getDriverRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(driverRating)
                .divide(BigDecimal.valueOf(rides.size() + 1), 2, BigDecimal.ROUND_HALF_UP);
        driverClient.updateRating(ride.getDriverId(), averageRating);
        return rideRepository.save(ride);
    }

    private BigDecimal calculateCost(Ride ride) {
        BigDecimal cost =  ride.getDemandValue().multiply(ride.getDestination());
        if (ride.getPromocode() != null) {
            Optional<Promocode> promocode = promocodeService.findByName(ride.getPromocode());
            if (isPromocodeValid(ride, promocode)) {
                cost = cost.multiply(
                        (BigDecimal.valueOf(100).subtract(new BigDecimal(promocode.get().getDiscount())))
                                .divide(BigDecimal.valueOf(100.0)));
            }
        }
        return cost.multiply(ride.getDemandValue());
    }

    private BigDecimal calculateDestination(Ride ride, Message message) {
        Coordinate coordinateFrom = coordinateService.getCoordinates(ride.getAddressFrom());
        message.setLatitude(coordinateFrom.getLatitude());
        message.setLongitude(coordinateFrom.getLongitude());
        Coordinate coordinateTo = coordinateService.getCoordinates(ride.getAddressTo());
        return coordinateService.calculateDistance(coordinateFrom.getLatitude(),
                coordinateFrom.getLongitude(), coordinateTo.getLatitude(), coordinateTo.getLongitude());
    }

    private boolean isPromocodeValid(Ride ride, Optional<Promocode> promocode) {
        if (promocode.isEmpty()) {
            return false;
        }
        Promocode p = promocode.get();
        if (ride.getDateTime().toLocalDate().isAfter(p.getDateTo()) ||
                ride.getDateTime().toLocalDate().isBefore(p.getDateFrom())) {
            return false;
        }
        return true;
    }

    private BigDecimal calculateDemandValue(Ride ride) {
        int hour = ride.getDateTime().getHour();
        if ((hour >= DemandProperties.HOURS_FOR_LOW_DEMAND_FROM &&
                hour < DemandProperties.HOURS_FOR_MEDIUM_DEMAND_FROM)) {
            return DemandProperties.LOW_DEMAND;
        } else if ((hour >= DemandProperties.HOURS_FOR_MEDIUM_DEMAND_FROM &&
                hour < DemandProperties.HOURS_FOR_HIGH_DEMAND_FROM)) {
            return  DemandProperties.MEDIUM_DEMAND;
        } else {
            return DemandProperties.HUGH_DEMAND;
        }
    }

}
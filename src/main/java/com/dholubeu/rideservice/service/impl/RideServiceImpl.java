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
import com.dholubeu.rideservice.web.dto.RideDto;
import com.dholubeu.rideservice.web.mapper.RideMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.dholubeu.rideservice.util.Constants.RIDE_DOES_NOT_EXIST_BY_ID_MESSAGE;
import static com.dholubeu.rideservice.util.Constants.SCALE_VALUE;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {


    private final RideRepository rideRepository;
    private final PromocodeService promocodeService;
    private final CoordinateService coordinateService;
    private final DriverClient driverClient;
    private final PassengerClient passengerClient;
    private final KfProducer kfProducer;

    private final RideMapper rideMapper;

    @Override
    public RideDto create(RideDto rideDto) {
        Message message = new Message();
        Ride ride = rideMapper.toEntity(rideDto);
        ride.setDateTime(LocalDateTime.now());
        ride.setDemandValue(calculateDemandValue(ride)
                .setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP));
        ride.setDestination(calculateDestination(ride, message)
                .setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP));
        ride.setCost(calculateCost(ride)
                .setScale(SCALE_VALUE, BigDecimal.ROUND_HALF_UP));
        ride.setStatus(Ride.Status.NEW);
        ride = rideRepository.save(ride);
        message.setRideId(ride.getId());
        kfProducer.send(message);
        return rideMapper.toDto(ride);
    }

    @Override
    public RideDto findById(Long id) {
        var ride = rideRepository.findById(id).orElseThrow(
                () -> new ResourceDoesNotExistException(
                        String.format(RIDE_DOES_NOT_EXIST_BY_ID_MESSAGE, id)));
        return rideMapper.toDto(ride);
    }

    @Override
    public List<RideDto> findAllByPassengerId(Long passengerId) {
        var ride = rideRepository.findAllByPassengerId(passengerId);
        return rideMapper.toDto(ride);
    }

    @Override
    public List<RideDto> findAllByDriverId(Long driverId) {

        var ride = rideRepository.findAllByDriverId(driverId);
        return rideMapper.toDto(ride);
    }

    @Override
    public RideDto updateStatus(Long id, Ride.Status status) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException(""));
        ride.setStatus(status);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    public RideDto setDriverId(Long id, Long driverId) {
        RideDto rideDto = updateStatus(id, Ride.Status.ACCEPTED);
        rideDto.setDriverId(driverId);
        return rideDto;
    }

    @Override
    public RideDto setPassengerRating(Long id, BigDecimal passengerRating) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException(""));
        ride.setPassengerRating(passengerRating);
        List<RideDto> rides = findAllByPassengerId(ride.getPassengerId());
        BigDecimal averageRating = rides.stream()
                .map(RideDto::getPassengerRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(passengerRating)
                .divide(BigDecimal.valueOf(rides.size() + 1), 2, BigDecimal.ROUND_HALF_UP);
        passengerClient.updateRating(ride.getPassengerId(), averageRating);
        return rideMapper.toDto(ride);
    }

    @Override
    public RideDto setDriverRating(Long id, BigDecimal driverRating) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException(""));
        ride.setDriverRating(driverRating);
        List<RideDto> rides = findAllByPassengerId(ride.getDriverId());
        BigDecimal averageRating = rides.stream()
                .map(RideDto::getDriverRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(driverRating)
                .divide(BigDecimal.valueOf(rides.size() + 1), 2, BigDecimal.ROUND_HALF_UP);
        driverClient.updateRating(ride.getDriverId(), averageRating);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    private BigDecimal calculateCost(Ride ride) {
        BigDecimal cost = ride.getDemandValue().multiply(ride.getDestination());
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
            return DemandProperties.MEDIUM_DEMAND;
        } else {
            return DemandProperties.HUGH_DEMAND;
        }
    }

}
package com.dholubeu.rideservice.web.controller;

import com.dholubeu.rideservice.domain.Promocode;
import com.dholubeu.rideservice.domain.Ride;
import com.dholubeu.rideservice.service.PromocodeService;
import com.dholubeu.rideservice.service.RideService;
import com.dholubeu.rideservice.web.dto.PromocodeDto;
import com.dholubeu.rideservice.web.dto.RideDto;
import com.dholubeu.rideservice.web.mapper.PromocodeMapper;
import com.dholubeu.rideservice.web.mapper.RideMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final PromocodeService promocodeService;
    private final RideMapper rideMapper;
    private final PromocodeMapper promocodeMapper;

    @PostMapping("/promocodes")
    @ResponseStatus(HttpStatus.CREATED)
    public PromocodeDto createPromocode(@RequestBody @Validated PromocodeDto promocodeDto) {
        Promocode promocode = promocodeMapper.toEntity(promocodeDto);
        promocode = promocodeService.create(promocode);
        return promocodeMapper.toDto(promocode);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RideDto create(@RequestBody @Validated RideDto rideDto) {
        Ride ride = rideMapper.toEntity(rideDto);
        ride = rideService.create(ride);
        return rideMapper.toDto(ride);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideDto findById(@PathVariable Long id) {
        Ride ride = rideService.findById(id);
        return rideMapper.toDto(ride);
    }


    @GetMapping("passengers/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RideDto> findAllByPassengerId(@PathVariable Long passengerId) {
        List<Ride> rides = rideService.findAllByPassengerId(passengerId);
        return rideMapper.toDto(rides);
    }

    @GetMapping("drivers/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RideDto> findAllByDriverId(@PathVariable Long driverId) {
        List<Ride> rides = rideService.findAllByDriverId(driverId);
        return rideMapper.toDto(rides);
    }

    @PutMapping("/statuses/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto updateStatus(@PathVariable Long id, @RequestParam Ride.Status status) {
        Ride ride = rideService.updateStatus(id, status);
        return rideMapper.toDto(ride);
    }

    @PutMapping("/{id}/drivers/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto setDriverId(@PathVariable Long id, @PathVariable Long driverId) {
        Ride ride = rideService.setDriverId(id, driverId);
        return rideMapper.toDto(ride);
    }

    @PutMapping("/{id}/passengers/ratings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto setPassengerRating(@PathVariable Long id,
                                      @RequestParam BigDecimal passengerRating) {
        Ride ride = rideService.setPassengerRating(id, passengerRating);
        return rideMapper.toDto(ride);
    }

    @PutMapping("/{id}/drivers/ratings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto setDriverRating(@PathVariable Long id,
                                   @RequestParam BigDecimal driverRating) {
        Ride ride = rideService.setDriverRating(id, driverRating);
        return rideMapper.toDto(ride);
    }

}
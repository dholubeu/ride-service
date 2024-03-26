package com.dholubeu.rideservice.service;

import com.dholubeu.rideservice.domain.Status;
import com.dholubeu.rideservice.web.dto.RideDto;

import java.math.BigDecimal;
import java.util.List;

public interface RideService {

    RideDto create(RideDto rideDto);

    RideDto findById(Long id);

    List<RideDto> findAllByPassengerId(Long passengerId);

    List<RideDto> findAllByDriverId(Long driverId);

    RideDto updateStatus(Long id, Status status);

    RideDto setDriverId(Long rideId, Long driverId);

    RideDto setPassengerRating(Long id, BigDecimal passengerRating);

    RideDto setDriverRating(Long id, BigDecimal driverRating);

}
package com.dholubeu.rideservice.service;

import com.dholubeu.rideservice.domain.Ride;

import java.math.BigDecimal;
import java.util.List;

public interface RideService {

    Ride create(Ride ride);

    Ride findById(Long id);

    List<Ride> findAllByPassengerId(Long passengerId);

    List<Ride> findAllByDriverId(Long driverId);

    Ride updateStatus(Long id, Ride.Status status);

    Ride setDriverId(Long rideId, Long driverId);

    Ride setPassengerRating(Long id, BigDecimal passengerRating);

    Ride setDriverRating(Long id, BigDecimal driverRating);

}
package com.dholubeu.rideservice.repository;

import com.dholubeu.rideservice.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findAllByDriverId(Long driverId);

    List<Ride> findAllByPassengerId(Long passengerId);

}
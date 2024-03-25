package com.dholubeu.rideservice.service;

import com.dholubeu.rideservice.domain.Coordinate;

import java.math.BigDecimal;

public interface CoordinateService {

    Coordinate getCoordinates(String currentAddress);

    BigDecimal calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2);

}
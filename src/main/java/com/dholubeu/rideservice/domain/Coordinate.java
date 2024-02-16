package com.dholubeu.rideservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Coordinate {

    private BigDecimal longitude;
    private BigDecimal latitude;

}
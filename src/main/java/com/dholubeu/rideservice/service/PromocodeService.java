package com.dholubeu.rideservice.service;

import com.dholubeu.rideservice.domain.Promocode;

import java.util.Optional;

public interface PromocodeService {

    Promocode create(Promocode promocode);

    Optional<Promocode> findByName(String name);

}
package com.dholubeu.rideservice.service;

import com.dholubeu.rideservice.domain.Promocode;
import com.dholubeu.rideservice.web.dto.PromocodeDto;

import java.util.Optional;

public interface PromocodeService {

    PromocodeDto create(PromocodeDto promocodeDto);

    Optional<Promocode> findByName(String name);

}
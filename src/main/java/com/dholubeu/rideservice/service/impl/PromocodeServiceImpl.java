package com.dholubeu.rideservice.service.impl;

import com.dholubeu.rideservice.domain.Promocode;
import com.dholubeu.rideservice.repository.PromocodeRepository;
import com.dholubeu.rideservice.service.PromocodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromocodeServiceImpl implements PromocodeService {

    public static final String RESOURCE_DOES_NOT_EXIST_BY_ID_MESSAGE = "Promocode with name %s does not exist";

    private final PromocodeRepository promocodeRepository;

    @Override
    public Promocode create(Promocode promocode) {
        promocode.setId(UUID.randomUUID().toString());
        return promocodeRepository.save(promocode);
    }

    @Override
    public Optional<Promocode> findByName(String name) {
        return promocodeRepository.findByName(name);
    }

}
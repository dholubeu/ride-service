package com.dholubeu.rideservice.service.impl;

import com.dholubeu.rideservice.domain.Promocode;
import com.dholubeu.rideservice.repository.PromocodeRepository;
import com.dholubeu.rideservice.service.PromocodeService;
import com.dholubeu.rideservice.web.dto.PromocodeDto;
import com.dholubeu.rideservice.web.mapper.PromocodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromocodeServiceImpl implements PromocodeService {

    private final PromocodeRepository promocodeRepository;
    private final PromocodeMapper promocodeMapper;

    @Override
    public PromocodeDto create(PromocodeDto promocodeDto) {
        Promocode promocode = promocodeMapper.toEntity(promocodeDto);
        promocode.setId(UUID.randomUUID().toString());
         promocodeRepository.save(promocode);
        return promocodeMapper.toDto(promocode);

    }

    @Override
    public Optional<Promocode> findByName(String name) {
        return promocodeRepository.findByName(name);
    }

}
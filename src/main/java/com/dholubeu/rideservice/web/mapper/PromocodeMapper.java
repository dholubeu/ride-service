package com.dholubeu.rideservice.web.mapper;

import com.dholubeu.rideservice.domain.Promocode;
import com.dholubeu.rideservice.web.dto.PromocodeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromocodeMapper {

    PromocodeDto toDto(Promocode promocode);

    Promocode toEntity(PromocodeDto promocodeDto);

}
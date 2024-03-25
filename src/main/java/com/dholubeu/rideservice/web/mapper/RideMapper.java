package com.dholubeu.rideservice.web.mapper;

import com.dholubeu.rideservice.domain.Ride;
import com.dholubeu.rideservice.web.dto.RideDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RideMapper {

    RideDto toDto(Ride ride);

    List<RideDto> toDto(List<Ride> rides);

    Ride toEntity(RideDto rideDto);

    List<Ride> toEntity(List<RideDto> rideDtos);

}

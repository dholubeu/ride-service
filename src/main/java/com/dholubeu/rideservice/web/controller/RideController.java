package com.dholubeu.rideservice.web.controller;


import com.dholubeu.rideservice.domain.Status;
import com.dholubeu.rideservice.service.PromocodeService;
import com.dholubeu.rideservice.service.RideService;
import com.dholubeu.rideservice.web.dto.PromocodeDto;
import com.dholubeu.rideservice.web.dto.RideDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final PromocodeService promocodeService;

    @PostMapping("/promocodes")
    @ResponseStatus(HttpStatus.CREATED)
    public PromocodeDto createPromocode(@RequestBody @Validated PromocodeDto promocodeDto) {
        return promocodeService.create(promocodeDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideDto create(@RequestBody @Validated RideDto rideDto) {
        return rideService.create(rideDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideDto findById(@PathVariable Long id) {
        return rideService.findById(id);
    }

    @GetMapping("passengers/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RideDto> findAllByPassengerId(@PathVariable Long passengerId) {
        return rideService.findAllByPassengerId(passengerId);
    }

    @GetMapping("drivers/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RideDto> findAllByDriverId(@PathVariable Long driverId) {
        return rideService.findAllByDriverId(driverId);
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto updateStatus(@PathVariable Long id, @RequestParam Status status) {
        return rideService.updateStatus(id, status);
    }

    @PutMapping("/{id}/drivers/{driverId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto setDriverId(@PathVariable Long id, @PathVariable Long driverId) {
        return rideService.setDriverId(id, driverId);
    }

    @PutMapping("/{id}/passengers/ratings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto setPassengerRating(@PathVariable Long id,
                                      @RequestParam BigDecimal passengerRating) {
        return rideService.setPassengerRating(id,passengerRating);
    }

    @PutMapping("/{id}/drivers/ratings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RideDto setDriverRating(@PathVariable Long id,
                                   @RequestParam BigDecimal driverRating) {
        return rideService.setDriverRating(id,driverRating);
    }

}
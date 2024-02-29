package com.dholubeu.rideservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "passengers",
        url = "http://${open.feign.passengers.host}:8013/passengers/api/v1/passengers")
public interface PassengerClient {

    @PutMapping("/{id}/ratings")
    void updateRating(@PathVariable Long id, @RequestParam BigDecimal rating);

}
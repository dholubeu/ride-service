package com.dholubeu.rideservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "drivers",
        url = "http://${open.feign.drivers.host}:8013/drivers/api/v1/drivers")
public interface DriverClient {

    @PutMapping("/{id}/ratings")
    void updateRating(@PathVariable Long id, @RequestParam BigDecimal rating);

}
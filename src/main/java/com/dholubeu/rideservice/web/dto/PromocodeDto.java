package com.dholubeu.rideservice.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class PromocodeDto {

    private String id;

    @NotEmpty(message = "Keyword is required")
    private String name;

    @Min(value = 1, message = "Discount must be more than 1%")
    @Max(value = 50, message = "Discount must be less than 50%")
    @NotNull(message = "Discount is required")
    private Integer discount;

    @Future(message = "Invalid date")
    private LocalDate dateFrom;

    @Future(message = "Invalid date")
    private LocalDate dateTo;

}
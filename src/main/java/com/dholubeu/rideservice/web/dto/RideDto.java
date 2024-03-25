package com.dholubeu.rideservice.web.dto;

import com.dholubeu.rideservice.domain.Ride;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class RideDto {

    private Long id;

    @NotEmpty(message = "Address from is required")
    private String addressFrom;

    @NotEmpty(message = "Address to is required")
    private String addressTo;

    @NotNull(message = "Passenger is required")
    private Long passengerId;

    private Long driverId;

    @NotNull(message = "Payment method is required")
    @Enumerated(EnumType.STRING)
    private Ride.PaymentMethod paymentMethod;

    private BigDecimal destination;

    private BigDecimal demandValue;

    private BigDecimal cost;

    private Ride.Status status;

    @DecimalMin(value = "1", message = "Rating must be greater than or equal to 1")
    @DecimalMax(value = "5", message = "Rating must be less than or equal to 5")
    private BigDecimal passengerRating;

    @DecimalMin(value = "1", message = "Rating must be greater than or equal to 1")
    @DecimalMax(value = "5", message = "Rating must be less than or equal to 5")
    private BigDecimal driverRating;

    private String promocode;

    private LocalDateTime dateTime;

}
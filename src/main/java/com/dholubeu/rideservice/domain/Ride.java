package com.dholubeu.rideservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_from")
    private String addressFrom;

    @Column(name = "address_to")
    private String addressTo;

    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "driver_id")
    private Long driverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    private BigDecimal destination;

    @Column(name = "demand_value")
    private BigDecimal demandValue;

    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "passenger_rating")
    private BigDecimal passengerRating;

    @Column(name = "driver_rating")
    private BigDecimal driverRating;

    private String promocode;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public enum PaymentMethod {
        CARD,
        CASH
    }

    public enum Status {
        NEW,
        ACCEPTED,
        IN_PROCESS,
        FINISHED,
        CLOSED
    }

}
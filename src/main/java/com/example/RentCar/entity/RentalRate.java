package com.example.RentCar.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rental_rates")

public class RentalRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
      Long id;

    @Enumerated(EnumType.STRING)
    RateType rateType;

    Boolean hasDriver;
    BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "car_id")
    Car car;

    public enum RateType{
        hour, day, month
    }


}

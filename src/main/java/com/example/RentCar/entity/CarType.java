package com.example.RentCar.entity;

import jakarta.persistence.*;

@Entity
@Table(name= "car_types")
public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
}

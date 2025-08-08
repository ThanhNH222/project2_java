package com.example.RentCar.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "has_driver")
    private Boolean hasDriver;

    String status;
    String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "car_type_id")
    CarType carType;


    @ManyToOne
    @JoinColumn(name= "brand_id")
    CarBrand brand;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RentalRate> rentalRates = new ArrayList<>();

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    CarDeposit carDeposit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Boolean getHasDriver() {
        return hasDriver;
    }

    public void setHasDriver(Boolean hasDriver) {
        this.hasDriver = hasDriver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public CarBrand getBrand() {
        return brand;
    }

    public void setBrand(CarBrand brand) {
        this.brand = brand;
    }

    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    public CarDeposit getCarDeposit() {
        return carDeposit;
    }

    public void setCarDeposit(CarDeposit carDeposit) {
        this.carDeposit = carDeposit;
    }
}


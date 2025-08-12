package com.example.RentCar.service;

import com.example.RentCar.entity.Car;
import com.example.RentCar.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Lấy tất cả xe
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Lấy chi tiết xe theo ID
    public Car getCarById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.orElse(null);
    }

    // Tìm kiếm xe
    public List<Car> searchCars(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return carRepository.findAll();
        }
        return carRepository.searchCars(keyword);
    }
}

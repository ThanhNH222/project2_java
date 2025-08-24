package com.example.RentCar.service;

import com.example.RentCar.entity.Car;
import com.example.RentCar.repository.CarRepository;
import com.example.RentCar.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public CarService(CarRepository carRepository, RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
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

    // Kiểm tra xem xe có bị trùng lịch đặt không
    public boolean isCarAvailable(Long carId, LocalDateTime pickup, LocalDateTime dropoff) {
        return rentalRepository.findOverlappingRentals(carId, pickup, dropoff).isEmpty();
    }
}

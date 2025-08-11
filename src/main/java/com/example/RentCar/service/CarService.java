package com.example.RentCar.service;

import com.example.RentCar.entity.Car;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class CarService {

    public List<Car> getAllCars() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/static/data/data.json");
            return mapper.readValue(inputStream, new TypeReference<List<Car>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Car getCarById(Long id) {
        return getAllCars().stream()
                .filter(car -> car.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Car> searchCars(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCars();
        }
        String lowerKeyword = keyword.toLowerCase();

        return getAllCars().stream()
                .filter(car ->
                        car.getName().toLowerCase().contains(lowerKeyword) ||
                                (car.getCarType() != null && car.getCarType().getName().toLowerCase().contains(lowerKeyword)) ||
                                (car.getBrand() != null && car.getBrand().getName().toLowerCase().contains(lowerKeyword))
                )
                .toList();
    }
}

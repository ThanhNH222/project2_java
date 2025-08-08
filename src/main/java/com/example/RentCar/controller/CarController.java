package com.example.RentCar.controller;

import com.example.RentCar.entity.Car;
import com.example.RentCar.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CarController {

    private CarRepository carRepository;

    @GetMapping("/search")
    public String searchCars(@RequestParam("keyword") String keyword, Model model) {
        List<Car> cars = carRepository.searchCars(keyword);
        model.addAttribute("cars", cars);
        model.addAttribute("keyword", keyword);
        return "car/search-results";
    }
}

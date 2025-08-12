package com.example.RentCar.controller;

import com.example.RentCar.entity.Car;
import com.example.RentCar.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }



    @GetMapping("/car/{id}")
    public String carDetail(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id);
        if (car == null) {
            return "404"; // nếu không tìm thấy xe thì trả về trang 404
        }
        model.addAttribute("car", car);
        return "car/car-detail"; // file car-detail.html sẽ hiển thị thông tin xe
    }


    // Tìm kiếm xe
    @GetMapping("/search")
    public String searchCars(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Car> cars = carService.searchCars(keyword);
        model.addAttribute("cars", cars);
        model.addAttribute("keyword", keyword);
        return "/car/search";
    }
}

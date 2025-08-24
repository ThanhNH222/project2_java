package com.example.RentCar.controller.admin;

import com.example.RentCar.repository.RentalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    private final RentalRepository rentalRepository;

    public AdminRentalController(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    // Danh sách thuê xe
    @GetMapping
    public String listRentals(Model model) {
        model.addAttribute("rentals", rentalRepository.findAll());
        return "admin/rentals/list"; // file templates/admin/rentals/list.html
    }
}

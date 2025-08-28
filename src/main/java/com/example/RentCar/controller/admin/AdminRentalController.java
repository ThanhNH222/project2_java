package com.example.RentCar.controller.admin;

import com.example.RentCar.entity.Rental;
import com.example.RentCar.repository.RentalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // Hiện form thêm hợp đồng
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("rental", new Rental());
        return "admin/rentals/form"; // file templates/admin/rentals/form.html
    }

    // Lưu hợp đồng
    @PostMapping("/save")
    public String saveRental(@ModelAttribute("rental") Rental rental) {
        rentalRepository.save(rental);
        return "redirect:/admin/rentals"; // quay lại list sau khi lưu
    }
}

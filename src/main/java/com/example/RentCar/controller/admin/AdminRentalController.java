package com.example.RentCar.controller.admin;

import com.example.RentCar.entity.Rental;
import com.example.RentCar.entity.Car;
import com.example.RentCar.entity.Customer;
import com.example.RentCar.service.RentalService;
import com.example.RentCar.service.CarService;
import com.example.RentCar.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/rentals")
public class AdminRentalController {

    private final RentalService rentalService;
    private final CarService carService;
    private final CustomerService customerService;

    public AdminRentalController(RentalService rentalService,
                                 CarService carService,
                                 CustomerService customerService) {
        this.rentalService = rentalService;
        this.carService = carService;
        this.customerService = customerService;
    }

    // 📌 Danh sách thuê xe
    @GetMapping
    public String listRentals(Model model,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "success", required = false) String success) {
        List<Rental> rentals = rentalService.getAllRentals(); // ✅ đúng method
        model.addAttribute("rentals", rentals);

        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        if (success != null) {
            model.addAttribute("successMessage", "Lưu hợp đồng thành công!");
        }

        return "admin/rentals/list";
    }

    // 📌 Hiện form thêm hợp đồng
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("rental", new Rental());

        List<Car> cars = carService.getAllCars(); // ✅ thay findAll() bằng getAllCars()
        List<Customer> customers = customerService.getAllCustomers(); // ✅ thay findAll() bằng getAllCustomers()

        model.addAttribute("cars", cars);
        model.addAttribute("customers", customers);

        return "admin/rentals/form";
    }

    // 📌 Hiện form sửa hợp đồng
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Rental rental = rentalService.getRentalById(id); // ✅ thay findById() bằng getRentalById()

        List<Car> cars = carService.getAllCars();
        List<Customer> customers = customerService.getAllCustomers();

        model.addAttribute("rental", rental);
        model.addAttribute("cars", cars);
        model.addAttribute("customers", customers);

        return "admin/rentals/form";
    }

    // 📌 Lưu hợp đồng (có kiểm tra trùng lịch)
    @PostMapping("/save")
    public String saveRental(@ModelAttribute("rental") Rental rental) {
        try {
            rentalService.createRental(rental);
            return "redirect:/admin/rentals?success=1";
        } catch (RuntimeException e) {
            return "redirect:/admin/rentals?error=" + e.getMessage();
        }
    }

    // 📌 Cập nhật trạng thái thuê xe
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") String status) {
        rentalService.updateStatus(id, status);
        return "redirect:/admin/rentals";
    }
}

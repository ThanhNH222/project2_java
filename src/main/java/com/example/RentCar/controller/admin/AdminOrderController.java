package com.example.RentCar.controller.admin;

import com.example.RentCar.entity.ImageType;
import com.example.RentCar.entity.Rental;
import com.example.RentCar.entity.RentalImage;
import com.example.RentCar.repository.RentalImageRepository;
import com.example.RentCar.repository.RentalRepository;
import com.example.RentCar.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/orders")   // <— prefix chung cho toàn bộ controller
public class AdminOrderController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalImageRepository rentalImageRepository;

    @Autowired
    private EmailService emailService;

    /** ---------------- Danh sách đơn thuê ---------------- */
    @GetMapping
    public String listOrders(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String status,
                             Model model) {

        List<Rental> orders;

        if (keyword != null && !keyword.isEmpty() && status != null && !status.isEmpty()) {
            orders = rentalRepository
                    .findByStatusAndCustomerNameContainingIgnoreCaseOrStatusAndCustomerEmailContainingIgnoreCase(
                            status, keyword, status, keyword);
        } else if (keyword != null && !keyword.isEmpty()) {
            orders = rentalRepository
                    .findByCustomerNameContainingIgnoreCaseOrCustomerEmailContainingIgnoreCase(keyword, keyword);
        } else if (status != null && !status.isEmpty()) {
            orders = rentalRepository.findByStatus(status);
        } else {
            orders = rentalRepository.findAll();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "admin/orders/index";   // view: src/main/resources/templates/admin/orders/index.html
    }

    /** ---------------- Chi tiết một đơn thuê ---------------- */
    @GetMapping("/detail/{id}")
    public String rentalDetail(@PathVariable Long id, Model model) {

        Rental order = rentalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn thuê"));

        List<RentalImage> contractImages =
                rentalImageRepository.findByRentalIdAndImageType(id, ImageType.CONTRACT);
        List<RentalImage> handoverImages =
                rentalImageRepository.findByRentalIdAndImageType(id, ImageType.HANDOVER);
        List<RentalImage> returnImages =
                rentalImageRepository.findByRentalIdAndImageType(id, ImageType.RETURN);

        model.addAttribute("order", order);
        model.addAttribute("contractImages", contractImages);
        model.addAttribute("handoverImages", handoverImages);
        model.addAttribute("returnImages", returnImages);

        // view: src/main/resources/templates/admin/orders/detail.html
        return "admin/orders/detail";
    }

    /** ---------------- Cập nhật trạng thái ---------------- */
    @PostMapping("/admin/orders/update-status")
    public String updateStatus(@RequestParam Long orderId,
                               @RequestParam String status) throws MessagingException {

        Rental rental = rentalRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));

        rental.setStatus(status);

        if ("DELIVERED".equalsIgnoreCase(status)) {
            String token = UUID.randomUUID().toString();
            rental.setConfirmationToken(token);
            String confirmUrl = "https://yourdomain.com/rental/confirm?token=" + token;
            emailService.sendStatusUpdateEmail(rental, confirmUrl);
        }

        rentalRepository.save(rental);

        // Quay lại danh sách đơn thuê
        return "redirect:/admin/orders";
    }
}

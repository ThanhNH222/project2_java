package com.example.RentCar.controller;

import com.example.RentCar.service.PaypalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Locale;

@Controller
public class PaypalController {

    private final PaypalService paypalService;

    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    @GetMapping("/pay")
    public String pay(@RequestParam(required = false) Double amount,
                      @RequestParam(defaultValue = "USD") String currency) throws Exception {

        if (amount == null) {
            return "redirect:/payment?error=missingAmount";
        }

        // Bắt buộc dùng Locale.US để tránh "10,00"
        String formattedAmount = String.format(Locale.US, "%.2f", amount);

        String approvalLink = paypalService.createOrder(
                formattedAmount,
                currency,
                "http://localhost:8080/pay/success",
                "http://localhost:8080/pay/cancel"
        );

        return "redirect:" + approvalLink;
    }

    @GetMapping("/pay/success")
    public String success(@RequestParam("token") String orderId, Model model) throws Exception {
        var response = paypalService.captureOrder(orderId);
        model.addAttribute("transactionId", response.result().id());

        return "car/payment-success";
    }

    @GetMapping("/pay/cancel")
    public String cancel() {
        return "car/payment-cancel";
    }
}

package com.example.RentCar.controller;

import com.example.RentCar.entity.*;
import com.example.RentCar.repository.*;
import com.example.RentCar.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Controller
public class PaymentController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RentalRateRepository rentalRateRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaymentRepository paymentRepository;


    private static final double SHIPPING_FEE = 22222;

    /** Hiển thị trang thanh toán */
    @PostMapping("/payment")
    public String paymentPage(@RequestParam Long carId,
                              @RequestParam String pickupDate,
                              @RequestParam String pickupTime,
                              @RequestParam String dropoffDate,
                              @RequestParam String dropoffTime,
                              @RequestParam String rentalType,
                              @RequestParam(required = false) String pickupLocation,
                              @RequestParam(required = false) String dropoffLocation,
                              @RequestParam boolean isStorePickup,
                              @RequestParam(required = false) String storeLocation,
                              @RequestParam(required = false) String customerName,
                              @RequestParam(required = false) String customerPhone,
                              @RequestParam(required = false) String customerEmail,
                              Model model) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDate + "T" + pickupTime);
        LocalDateTime dropoffDateTime = LocalDateTime.parse(dropoffDate + "T" + dropoffTime);

        RentalRate.RateType rateTypeEnum = parseRateType(rentalType);
        RentalRate rate = rentalRateRepository.findByCarIdAndRateType(carId, rateTypeEnum);
        if (rate == null) throw new RuntimeException("Không tìm thấy hình thức thuê này");

        Store store = storeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng"));

        if (pickupLocation == null || pickupLocation.isEmpty()) {
            pickupLocation = store.getName() + " – " + store.getAddress();
        }
        if (dropoffLocation == null || dropoffLocation.isEmpty()) {
            dropoffLocation = store.getName() + " – " + store.getAddress();
        }

        long rentalUnits = calculateRentalUnits(pickupDateTime, dropoffDateTime, rateTypeEnum);
        BigDecimal unitPrice = rate.getPrice();
        BigDecimal rentalPrice = unitPrice.multiply(BigDecimal.valueOf(rentalUnits));
        BigDecimal depositAmount = car.getDepositAmount();
        BigDecimal shippingFee = isStorePickup ? BigDecimal.ZERO : BigDecimal.valueOf(SHIPPING_FEE);
        BigDecimal totalPayment = depositAmount.add(shippingFee);

        model.addAttribute("car", car);
        model.addAttribute("rentalType", rentalType);
        model.addAttribute("pickupDateTime", pickupDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        model.addAttribute("dropoffDateTime", dropoffDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        model.addAttribute("rentalPrice", rentalPrice);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("depositAmount", depositAmount);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("pickupLocation", pickupLocation);
        model.addAttribute("dropoffLocation", dropoffLocation);
        model.addAttribute("isStorePickup", isStorePickup);
        model.addAttribute("storeLocation", storeLocation);
        model.addAttribute("customerName", customerName);
        model.addAttribute("customerPhone", customerPhone);
        model.addAttribute("customerEmail", customerEmail);


        return "car/payment";
    }

    /** Xử lý form thanh toán */
    @PostMapping("/payment-success")
    public String confirmPayment(@RequestParam Long carId,
                                 @RequestParam String rentalType,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime pickupDateTime,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime dropoffDateTime,
                                 @RequestParam BigDecimal shippingFee,
                                 @RequestParam BigDecimal depositAmount,
                                 @RequestParam String customerName,
                                 @RequestParam String customerPhone,
                                 @RequestParam String customerEmail,
                                 @RequestParam String paymentMethod,

                                 @RequestParam String pickupLocation,
                                 @RequestParam String dropoffLocation,

                                 Model model) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        RentalRate.RateType rateTypeEnum = parseRateType(rentalType);
        RentalRate rate = rentalRateRepository.findByCarIdAndRateType(carId, rateTypeEnum);
        if (rate == null) throw new RuntimeException("Không tìm thấy hình thức thuê này");

        long rentalUnits = calculateRentalUnits(pickupDateTime, dropoffDateTime, rateTypeEnum);
        BigDecimal rentalPrice = rate.getPrice().multiply(BigDecimal.valueOf(rentalUnits));
        BigDecimal totalPayment = depositAmount.add(shippingFee);

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setRentalType(rentalType);
        rental.setPickupDateTime(pickupDateTime);
        rental.setDropoffDateTime(dropoffDateTime);
        rental.setDepositAmount(depositAmount);
        rental.setShippingFee(shippingFee);
        rental.setRentalPrice(rentalPrice);
        rental.setCustomerName(customerName);
        rental.setCustomerPhone(customerPhone);
        rental.setCustomerEmail(customerEmail);
        rental.setPaymentMethod(paymentMethod);
        rental.setTotalPayment(totalPayment);

        rental.setPickupLocation(pickupLocation);
        rental.setReturnLocation(dropoffLocation);


        rentalRepository.save(rental);
        model.addAttribute("rental", rental);


// ================= TÍNH TOÁN PAYMENT =================
        BigDecimal depositWithShip = depositAmount.add(shippingFee); // tiền cọc + phí ship
        BigDecimal refundAmount = depositWithShip.subtract(rentalPrice); // hoàn/trừ thêm

        Payment payment = new Payment();
        payment.setRental(rental);              // gắn vào rental vừa tạo
        payment.setRefundAmount(refundAmount);  // có thể âm (thu thêm) hoặc dương (hoàn lại)
        payment.setExtraCharges(BigDecimal.ZERO); // mặc định 0, admin sẽ chỉnh sau nếu có phát sinh

        paymentRepository.save(payment);

        model.addAttribute("payment", payment);



        try {
            emailService.sendRentalConfirmation(rental);
            System.out.println("✅ Email đã gửi tới: " + rental.getCustomerEmail());
        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi email: " + e.getMessage());
            e.printStackTrace();
        }



        return "car/payment-success";
    }

    private RentalRate.RateType parseRateType(String rentalType) {
        try {
            return RentalRate.RateType.valueOf(rentalType.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Loại hình thuê không hợp lệ: " + rentalType);
        }
    }

    private long calculateRentalUnits(LocalDateTime pickup, LocalDateTime dropoff, RentalRate.RateType rateType) {
        Duration duration = Duration.between(pickup, dropoff);
        switch (rateType) {
            case hour:
                return duration.toHours();
            case day:
                return duration.toDays();
            case month:
                return ChronoUnit.MONTHS.between(pickup.toLocalDate(), dropoff.toLocalDate());
            default:
                throw new IllegalArgumentException("Không hỗ trợ loại hình thuê: " + rateType);
        }
    }
}

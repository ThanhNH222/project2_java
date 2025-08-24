//package com.example.RentCar.entity;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "rentals")
//public class Rental {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // Liên kết với bảng cars
//    @ManyToOne
//    @JoinColumn(name = "car_id", nullable = false)
//    private Car car;
//
//    // Thông tin khách hàng (không có bảng users)
//    @Column(name = "customer_name", nullable = false)
//    private String customerName;
//
//    @Column(name = "customer_phone", nullable = false)
//    private String customerPhone;
//
//    @Column(name = "customer_email")
//    private String customerEmail;
//
//    @Column(name = "pickup_location", nullable = false)
//    private String pickupLocation;
//
//    @Column(name = "dropoff_location", nullable = false)
//    private String dropoffLocation;
//
//    @Column(name = "pickup_date", nullable = false)
//    private LocalDateTime pickupDate;
//
//    @Column(name = "dropoff_date", nullable = false)
//    private LocalDateTime dropoffDate;
//
//    @Column(nullable = false)
//    private String status; // Ví dụ: pending, confirmed, canceled
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    // ===== Getter & Setter =====
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Car getCar() { return car; }
//    public void setCar(Car car) { this.car = car; }
//
//    public String getCustomerName() { return customerName; }
//    public void setCustomerName(String customerName) { this.customerName = customerName; }
//
//    public String getCustomerPhone() { return customerPhone; }
//    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
//
//    public String getCustomerEmail() { return customerEmail; }
//    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
//
//    public String getPickupLocation() { return pickupLocation; }
//    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
//
//    public String getDropoffLocation() { return dropoffLocation; }
//    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }
//
//    public LocalDateTime getPickupDate() { return pickupDate; }
//    public void setPickupDate(LocalDateTime pickupDate) { this.pickupDate = pickupDate; }
//
//    public LocalDateTime getDropoffDate() { return dropoffDate; }
//    public void setDropoffDate(LocalDateTime dropoffDate) { this.dropoffDate = dropoffDate; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//
//    public LocalDateTime getUpdatedAt() { return updatedAt; }
//    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
//}

package com.example.RentCar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với bảng cars
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    // Thông tin khách hàng (không có bảng users)
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "return_location", nullable = false)
    private String returnLocation;

    // ✅ Sửa lại đúng với DB
    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "status")
    private String status; // pending, confirmed, canceled

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ===== Getter & Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getReturnLocation() { return returnLocation; }
    public void setReturnLocation(String returnLocation) { this.returnLocation = returnLocation; }

    public LocalDateTime getStartDatetime() { return startDatetime; }
    public void setStartDatetime(LocalDateTime startDatetime) { this.startDatetime = startDatetime; }

    public LocalDateTime getEndDatetime() { return endDatetime; }
    public void setEndDatetime(LocalDateTime endDatetime) { this.endDatetime = endDatetime; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}


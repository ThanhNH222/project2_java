package com.example.RentCar.repository;

import com.example.RentCar.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByConfirmationToken(String confirmationToken);
}


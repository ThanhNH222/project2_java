package com.example.RentCar.repository;

import com.example.RentCar.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {


    @Query("SELECT r FROM Rental r " +
            "WHERE r.car.id = :carId " +
            "AND (r.startDatetime < :endDatetime AND r.endDatetime > :startDatetime)")
    List<Rental> findOverlappingRentals(
            @Param("carId") Long carId,
            @Param("startDatetime") LocalDateTime startDatetime,
            @Param("endDatetime") LocalDateTime endDatetime
    );

}

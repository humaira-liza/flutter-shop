package com.myshop.backend.repository;

import com.myshop.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // USER HISTORY
    List<Order> findByUserEmail(String userEmail);

    // TOTAL REVENUE
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double getTotalRevenue();
}
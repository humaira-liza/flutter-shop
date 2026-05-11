package com.myshop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myshop.backend.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
package com.myshop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myshop.backend.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
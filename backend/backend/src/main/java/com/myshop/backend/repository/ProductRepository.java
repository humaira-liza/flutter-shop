package com.myshop.backend.repository;

import com.myshop.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long id);

    List<Product> findByCategoryIdIn(List<Long> ids);
}
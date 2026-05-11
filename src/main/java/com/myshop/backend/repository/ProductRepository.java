package com.myshop.backend.repository;

import com.myshop.backend.entity.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT DISTINCT p FROM Product p
        JOIN FETCH p.category
    """)
    List<Product> findAllWithCategory();

    @Query("""
        SELECT p FROM Product p
        WHERE p.category.id IN (
            SELECT c.id FROM Category c
            WHERE c.id = :id
            OR c.parent.id = :id
            OR c.parent.parent.id = :id
        )
    """)
    List<Product> findByCategoryDeep(@Param("id") Long id);
}
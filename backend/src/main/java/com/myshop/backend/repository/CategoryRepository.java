package com.myshop.backend.repository;

import com.myshop.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 🔥 parent load (safe)
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.parent")
    List<Category> findAllWithParent();

    // 🔥 main + children
    @Query("""
        SELECT DISTINCT c FROM Category c
        LEFT JOIN FETCH c.children
        WHERE c.parent IS NULL
    """)
    List<Category> findByParentIsNull();
}
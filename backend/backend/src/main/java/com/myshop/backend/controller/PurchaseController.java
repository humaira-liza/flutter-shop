package com.myshop.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.time.LocalDateTime;

import com.myshop.backend.entity.*;
import com.myshop.backend.repository.*;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepo;

    @Autowired
    private ProductRepository productRepo;

    // ✅ CREATE PURCHASE
    @PostMapping
    public Purchase create(@RequestBody Purchase purchase) {

        double total = 0;

        for (PurchaseItem item : purchase.getItems()) {

            Product p = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 🔥 STOCK ADD
            p.setStock(p.getStock() + item.getQuantity());
            productRepo.save(p);

            // 🔥 IMPORTANT (frontend show)
            item.setProductName(p.getName());
            item.setStock(p.getStock());

            total += item.getQuantity() * item.getUnitPrice();
        }

        purchase.setTotalAmount(total);
        purchase.setCreatedAt(LocalDateTime.now().toString());

        return purchaseRepo.save(purchase);
    }

    // ✅ GET ALL (history)
    @GetMapping
    public List<Purchase> getAll() {
        return purchaseRepo.findAll();
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        purchaseRepo.deleteById(id);
    }
}
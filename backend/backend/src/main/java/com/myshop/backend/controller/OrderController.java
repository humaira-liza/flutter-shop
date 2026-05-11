package com.myshop.backend.controller;

import com.myshop.backend.entity.Order;
import com.myshop.backend.entity.OrderItem;
import com.myshop.backend.entity.Product;
import com.myshop.backend.repository.OrderRepository;
import com.myshop.backend.repository.ProductRepository;
import com.myshop.backend.config.JwtUtil;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProductRepository productRepo; // 🔥 NEW

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    // 🔴 ALL ORDERS
    @GetMapping("/all")
    public List<Order> getAll(){
        return repo.findAll();
    }

    // 🟢 MY ORDERS
    @GetMapping("/my")
    public List<Order> myOrders(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        try {

            if (token == null || !token.startsWith("Bearer ")) {
                return List.of();
            }

            token = token.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);

            if (email == null) return List.of();

            if (email.equalsIgnoreCase("admin@gmail.com")) {
                return repo.findAll();
            }

            return repo.findByUserEmail(email);

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // 🛒 CREATE ORDER (🔥 STOCK FIXED)
    @PostMapping
    public Order create(
            @RequestBody Order o,
            @RequestHeader(value = "Authorization", required = false) String token
    ){

        // 🔐 TOKEN CHECK
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Token missing!");
        }

        token = token.replace("Bearer ", "");

        String email;
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token!");
        }

        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email not found!");
        }

        o.setUserEmail(email);
        o.setStatus("NEW");

        if (o.getItems() == null) {
            o.setItems(new ArrayList<>());
        }

        // 🔥🔥 MAIN PART (STOCK UPDATE)
        for (OrderItem item : o.getItems()) {

            item.setOrder(o);

            if (item.getProductId() != null) {

                Product p = productRepo.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                // ❌ safety check
                if (p.getStock() < item.getQuantity()) {
                    throw new RuntimeException("Stock not enough for " + p.getName());
                }

                // 🔥 STOCK REDUCE
                p.setStock(p.getStock() - item.getQuantity());

                productRepo.save(p);
            }
        }

        return repo.save(o);
    }

    // 🔁 STATUS UPDATE
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id){

        Order o = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        switch (o.getStatus()) {
            case "NEW": o.setStatus("PROCESSING"); break;
            case "PROCESSING": o.setStatus("DONE"); break;
        }

        return repo.save(o);
    }

    // ❌ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        repo.deleteById(id);
    }
}
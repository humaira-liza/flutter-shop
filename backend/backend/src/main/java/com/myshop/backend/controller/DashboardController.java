package com.myshop.backend.controller;

import com.myshop.backend.entity.User;
import com.myshop.backend.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final ComplaintRepository complaintRepo;
    private final UserRepository userRepo; // ✅ ADD

    public DashboardController(ProductRepository productRepo,
                               OrderRepository orderRepo,
                               ComplaintRepository complaintRepo,
                               UserRepository userRepo) { // ✅ ADD

        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.complaintRepo = complaintRepo;
        this.userRepo = userRepo; // ✅ ADD
    }

    @GetMapping
    public Map<String, Object> getDashboard(@RequestParam String email){

        // 🔥 ADMIN CHECK
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found ❌"));

        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Access Denied ❌");
        }

        Map<String, Object> map = new HashMap<>();

        double revenue = orderRepo.findAll()
                .stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0)
                .sum();

        map.put("totalProducts", productRepo.count());
        map.put("totalOrders", orderRepo.count());
        map.put("totalRevenue", revenue);
        map.put("totalComplaints", complaintRepo.count());

        return map;
    }
}
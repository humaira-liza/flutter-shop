package com.myshop.backend.controller;

import com.myshop.backend.entity.Order;
import com.myshop.backend.entity.Product;
import com.myshop.backend.repository.OrderRepository;
import com.myshop.backend.repository.ProductRepository;
import com.myshop.backend.config.JwtUtil;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProductRepository productRepository;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    // 🔴 ALL ORDERS
    @GetMapping("/all")
    public List<Order> getAll() {

        return repo.findAll();
    }

    // 🟢 MY ORDERS
    @GetMapping("/my")
    public List<Order> myOrders(

            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String token
    ) {

        try {

            if (token == null ||
                    !token.startsWith("Bearer ")) {

                return List.of();
            }

            token = token.replace("Bearer ", "");

            String email =
                    jwtUtil.extractEmail(token);

            // ADMIN সব order দেখতে পারবে
            if (email.equalsIgnoreCase(
                    "admin@gmail.com")) {

                return repo.findAll();
            }

            return repo.findByUserEmail(email);

        } catch (Exception e) {

            e.printStackTrace();

            return List.of();
        }
    }

    // 🛒 CREATE ORDER
    @PostMapping
    public Order create(

            @RequestBody Order o
    ) {

        try {

            // 🔥 EMAIL FIX
            if (o.getUserEmail() == null ||
                    o.getUserEmail().isEmpty()) {

                throw new RuntimeException(
                        "userEmail missing"
                );
            }

            o.setUserEmail(
                    o.getUserEmail()
            );

            // 🔥 DEFAULT STATUS
            o.setStatus("NEW");

            // 🔥 PAYMENT FIX
            if (o.getPaymentMethod() == null ||
                    o.getPaymentMethod().isEmpty()) {

                o.setPaymentMethod("COD");
            }

            // 🔥 ITEMS PROCESS
            if (o.getItems() != null) {

                o.getItems().forEach(i -> {

                    Product p =
                            productRepository.findById(
                                            i.getProductId()
                                    )
                                    .orElseThrow(
                                            () -> new RuntimeException(
                                                    "Product not found"
                                            )
                                    );

                    // PRODUCT INFO
                    i.setProductName(
                            p.getName()
                    );

                    i.setPrice(
                            p.getPrice()
                    );

                    i.setOrder(o);

                    // STOCK CHECK
                    if (p.getStock()
                            < i.getQuantity()) {

                        throw new RuntimeException(
                                "Out of stock for: "
                                        + p.getName()
                        );
                    }

                    // STOCK REDUCE
                    p.setStock(
                            p.getStock()
                                    - i.getQuantity()
                    );

                    // SOLD INCREASE FIX
                    Integer sold = p.getSold();

                    if (sold == null) {
                        sold = 0;
                    }

                    p.setSold(
                            sold + i.getQuantity()
                    );

                    productRepository.save(p);
                });
            }

            return repo.save(o);

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    e.getMessage()
            );
        }
    }

    // 🔁 STATUS UPDATE
    @PutMapping("/{id}/status")
    public Order updateStatus(
            @PathVariable Long id
    ) {

        Order o = repo.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Order not found"
                        )
                );

        switch (o.getStatus()) {

            case "NEW":

                o.setStatus("PROCESSING");

                break;

            case "PROCESSING":

                o.setStatus("DONE");

                break;
        }

        return repo.save(o);
    }

    // ✏ UPDATE ORDER
    @PutMapping("/{id}")
    public Order updateOrder(

            @PathVariable Long id,

            @RequestBody Order updated
    ) {

        Order o = repo.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Order not found"
                        )
                );

        o.setName(updated.getName());

        o.setPhone(updated.getPhone());

        o.setAddress(updated.getAddress());

        return repo.save(o);
    }

    // ❌ DELETE ORDER
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {

        repo.deleteById(id);
    }
}
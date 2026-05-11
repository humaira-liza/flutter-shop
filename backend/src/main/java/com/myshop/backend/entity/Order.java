package com.myshop.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String address;

    private Double totalAmount;

    private String status;
    private String paymentMethod;

    @Column(nullable = false)
    private String userEmail;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 🔥 IMPORTANT (no change needed here)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        if (this.status == null || this.status.isEmpty()) {
            this.status = "NEW";
        }

        if (this.userEmail == null || this.userEmail.isEmpty()) {
            throw new RuntimeException("userEmail is required!");
        }
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;

        if (items != null) {
            for (OrderItem i : items) {
                i.setOrder(this);
            }
        }
    }

    public List<OrderItem> getItems() { return items; }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
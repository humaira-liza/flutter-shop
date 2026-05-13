package com.myshop.backend.dto;

import java.util.List;

public class OrderRequest {

    public String name;

    public String phone;

    public String address;

    public Double totalAmount;

    public String paymentMethod;

    public List<OrderItemRequest> items;
}
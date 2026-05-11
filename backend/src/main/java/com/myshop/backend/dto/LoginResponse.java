package com.myshop.backend.dto;

import com.myshop.backend.entity.User;

public class LoginResponse {

    private Long id;
    private String email;
    private String role;

    public LoginResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
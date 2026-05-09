package com.myshop.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import com.myshop.backend.entity.User;
import com.myshop.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.myshop.backend.config.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔐 LOGIN
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User loginData) {

        User user = userRepo.findByEmail(loginData.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(loginData.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return Map.of(
                "email", user.getEmail(),
                "role", user.getRole(),
                "token", token
        );
    }

    // 🆕 REGISTER
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return Map.of("error", "Email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole("USER");
        }

        userRepo.save(user);

        return Map.of("message", "User registered successfully");
    }
}
package com.myshop.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import com.myshop.backend.entity.User;
import com.myshop.backend.repository.UserRepository;
import com.myshop.backend.config.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔐 LOGIN
    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody User loginData
    ) {

        try {

            User user = userRepo.findByEmail(
                    loginData.getEmail()
            ).orElseThrow(() ->
                    new RuntimeException(
                            "User not found"
                    )
            );

            // SIMPLE PASSWORD CHECK
            if (!user.getPassword().equals(
                    loginData.getPassword()
            )) {

                throw new RuntimeException(
                        "Wrong password"
                );
            }

            // 🔥 REAL JWT TOKEN
            String token =
                    jwtUtil.generateToken(
                            user.getEmail()
                    );

            return Map.of(

                    "token", token,

                    "email",
                    user.getEmail(),

                    "role",
                    user.getRole()
            );

        } catch (Exception e) {

            e.printStackTrace();

            return Map.of(
                    "error",
                    e.getMessage()
            );
        }
    }

    // 🆕 REGISTER
    @PostMapping("/register")
    public Map<String, Object> register(
            @RequestBody User user
    ) {

        if (userRepo.findByEmail(
                user.getEmail()
        ).isPresent()) {

            return Map.of(
                    "error",
                    "Email already exists"
            );
        }

        if (user.getRole() == null) {
            user.setRole("USER");
        }

        userRepo.save(user);

        return Map.of(
                "message",
                "User registered successfully"
        );
    }
}
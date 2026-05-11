package com.myshop.backend.controller;

import com.myshop.backend.entity.User;
import com.myshop.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;

    public UserController(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // 🔥 REGISTER USER (FIXED)
    @PostMapping
    public User create(@RequestBody User user) {

        user.setPassword(encoder.encode(user.getPassword()));

        // 🔥 THIS IS THE REAL FIX
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        return repo.save(user);
    }
}
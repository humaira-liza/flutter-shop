package com.myshop.backend.controller;

import com.myshop.backend.entity.Complaint;
import com.myshop.backend.repository.ComplaintRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    private final ComplaintRepository repository;

    public ComplaintController(ComplaintRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE complaint
    @PostMapping
    public Complaint create(@RequestBody Complaint complaint) {
        complaint.setCreatedAt(LocalDateTime.now());
        return repository.save(complaint);
    }

    // ✅ GET all complaints
    @GetMapping
    public List<Complaint> getAll() {
        return repository.findAll();
    }
}
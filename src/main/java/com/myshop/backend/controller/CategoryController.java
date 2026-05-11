package com.myshop.backend.controller;

import com.myshop.backend.entity.Category;
import com.myshop.backend.repository.CategoryRepository;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    // 🔥 TREE API (FINAL)
    @GetMapping("/tree")
    public List<Category> tree() {

        List<Category> all = repo.findAll();

        Map<Long, Category> map = new HashMap<>();

        for (Category c : all) {
            c.setChildren(new ArrayList<>());
            map.put(c.getId(), c);
        }

        List<Category> roots = new ArrayList<>();

        for (Category c : all) {

            if (c.getParent() == null) {
                roots.add(c);
            } else {
                Category parent = map.get(c.getParent().getId());
                if (parent != null) {
                    parent.getChildren().add(c);
                }
            }
        }

        return roots;
    }
}
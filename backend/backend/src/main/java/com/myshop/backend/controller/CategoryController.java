package com.myshop.backend.controller;

import com.myshop.backend.entity.Category;
import com.myshop.backend.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Category> main(){
        return repo.findByParentIsNull();
    }

    @GetMapping("/{id}/sub")
    public List<Category> sub(@PathVariable Long id){
        return repo.findByParentId(id);
    }
}
package com.myshop.backend.controller;

import com.myshop.backend.entity.Product;
import com.myshop.backend.repository.ProductRepository;
import com.myshop.backend.repository.CategoryRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository,
                             CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ✅ ALL PRODUCTS
    @GetMapping
    public List<Product> getAll() {

        List<Product> list = productRepository.findAll();

        list.sort(Comparator.comparing(p ->
                p.getName() == null ? "" : p.getName().toLowerCase()
        ));

        return list;
    }

    // ✅ ADD PRODUCT
    @PostMapping
    public Product add(@RequestBody Product p) {
        return productRepository.save(p);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product p) {

        Product old = productRepository.findById(id).orElseThrow();

        old.setName(p.getName());
        old.setPrice(p.getPrice());
        old.setImageUrl(p.getImageUrl());
        old.setStock(p.getStock());

        return productRepository.save(old);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    // ✅ CATEGORY FILTER
    @GetMapping("/by-category/{id}")
    public List<Product> getByCategory(@PathVariable Long id) {

        var children = categoryRepository.findByParentId(id);

        List<Long> ids = new ArrayList<>();
        ids.add(id);

        children.forEach(c -> ids.add(c.getId()));

        return productRepository.findByCategoryIdIn(ids);
    }

    // 🔥 FINAL IMAGE UPLOAD
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder
    ) {

        try {

            folder = folder.toLowerCase();

            String uploadDir = "uploads/" + folder + "/";
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            File saveFile = new File(uploadDir + fileName);
            file.transferTo(saveFile);

            // ✅ FINAL CLEAN URL
            String imageUrl = "http://localhost:8081/images/" + folder + "/" + fileName;

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed");
        }
    }
}
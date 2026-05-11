package com.myshop.backend.controller;

import com.myshop.backend.entity.Product;
import com.myshop.backend.repository.ProductRepository;
import com.myshop.backend.service.ProductService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService service;
    private final ProductRepository productRepository;

    public ProductController(
            ProductService service,
            ProductRepository productRepository
    ) {
        this.service = service;
        this.productRepository = productRepository;
    }

    // ✅ ALL PRODUCTS
    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    // ✅ CATEGORY FILTER
    @GetMapping("/category/{id}")
    public List<Product> getByCategory(
            @PathVariable Long id
    ) {
        return service.getByCategory(id);
    }

    // ✅ ADD PRODUCT
    @PostMapping
    public Product add(
            @RequestBody Product product
    ) {
        return service.save(product);
    }

    // ✅ UPDATE PRODUCT
    @PutMapping("/{id}")
    public Product updateProduct(

            @PathVariable Long id,

            @RequestBody Product product
    ) {

        Product old =
                productRepository
                        .findById(id)
                        .orElseThrow();

        old.setName(product.getName());
        old.setPrice(product.getPrice());
        old.setStock(product.getStock());

        if(product.getImageUrl() != null &&
                !product.getImageUrl().isEmpty()) {

            old.setImageUrl(
                    product.getImageUrl()
            );
        }

        return productRepository.save(old);
    }

    // ✅ DELETE PRODUCT
    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable Long id
    ) {
        productRepository.deleteById(id);
    }

    // 🔥 IMAGE UPLOAD
    @PostMapping("/upload")
    public String upload(

            @RequestParam("file")
            MultipartFile file,

            @RequestParam("folder")
            String folder
    ) {

        try {

            String uploadDir =
                    "uploads/" + folder + "/";

            java.io.File dir =
                    new java.io.File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename =
                    System.currentTimeMillis()
                            + "_"
                            + file.getOriginalFilename();

            Path path =
                    Paths.get(uploadDir + filename);

            Files.write(
                    path,
                    file.getBytes()
            );

            return folder + "/" + filename;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}
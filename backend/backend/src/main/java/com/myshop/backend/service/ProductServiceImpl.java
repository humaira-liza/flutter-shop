package com.myshop.backend.service;

import com.myshop.backend.entity.Product;
import com.myshop.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {

        Product existing = repository.findById(id).orElseThrow();

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setImageUrl(product.getImageUrl());
        existing.setCategory(product.getCategory());

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void decreaseStock(Long id) {

        Product p = repository.findById(id).orElseThrow();

        if (p.getStock() > 0) {
            p.setStock(p.getStock() - 1);
            repository.save(p);
        }
    }

    // ✅ Category wise product
    @Override
    public List<Product> getByCategory(Long id) {
        return repository.findByCategoryId(id);
    }

}
package com.myshop.backend.service;

import com.myshop.backend.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> getAll();
    List<Product> getByCategory(Long id);

    Product save(Product product);

    Product update(Long id, Product product);

    void delete(Long id);

    void decreaseStock(Long id);
}
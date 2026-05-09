package com.myshop.backend.service;

import com.myshop.backend.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getMainCategories();

    Category save(Category category);

}
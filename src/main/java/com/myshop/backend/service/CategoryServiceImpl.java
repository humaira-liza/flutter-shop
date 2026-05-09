package com.myshop.backend.service;

import com.myshop.backend.entity.Category;
import com.myshop.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Category> getMainCategories(){

        return repository.findByParentIsNull();

    }

    @Override
    public Category save(Category category){

        return repository.save(category);

    }

}
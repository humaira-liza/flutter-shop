package com.myshop.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {

    private Long id;
    private String name;
    private List<CategoryDTO> children = new ArrayList<>();

    public CategoryDTO() {}

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<CategoryDTO> getChildren() { return children; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setChildren(List<CategoryDTO> children) { this.children = children; }
}
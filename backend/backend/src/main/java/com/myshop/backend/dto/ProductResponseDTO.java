package com.myshop.backend.dto;

public record ProductResponseDTO(
        Long id,
        String name,
        String category,
        Double price
) {}
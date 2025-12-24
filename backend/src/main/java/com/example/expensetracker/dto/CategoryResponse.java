package com.example.expensetracker.dto;

import com.example.expensetracker.model.Category;
import lombok.Data;

import java.time.Instant;

@Data
public class CategoryResponse {
    private String id;
    private String userId;
    private String name;
    private Category.CategoryType type;
    private Instant createdAt;
}


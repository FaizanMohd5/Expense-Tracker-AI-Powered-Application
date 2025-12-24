package com.example.expensetracker.dto;

import com.example.expensetracker.model.Category;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private Category.CategoryType type;
}


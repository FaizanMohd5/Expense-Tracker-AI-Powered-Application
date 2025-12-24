package com.example.expensetracker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private String id;

    @Indexed
    private String userId; // Nullable: null for default categories
    private String name;
    private CategoryType type;
    private Instant createdAt;

    public enum CategoryType {
        EXPENSE, INCOME
    }
}


package com.example.expensetracker.repository;

import com.example.expensetracker.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByUserIdOrUserIdIsNull(String userId);
    Optional<Category> findByIdAndUserIdOrUserIdIsNull(String id, String userId);
    Optional<Category> findByUserIdAndName(String userId, String name);
    Optional<Category> findByUserIdIsNullAndName(String name);
    long countByUserIdAndId(String userId, String id);
}


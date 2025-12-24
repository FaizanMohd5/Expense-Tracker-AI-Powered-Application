package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryRequest;
import com.example.expensetracker.exception.BadRequestException;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    public List<Category> getUserCategories(String userId) {
        return categoryRepository.findByUserIdOrUserIdIsNull(userId);
    }

    public Category getCategoryById(String categoryId, String userId) {
        return categoryRepository.findByIdAndUserIdOrUserIdIsNull(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or unauthorized access"));
    }

    public Category createCategory(String userId, CategoryRequest request) {
        if (categoryRepository.findByUserIdAndName(userId, request.getName()).isPresent() ||
            (request.getType() != null && categoryRepository.findByUserIdIsNullAndName(request.getName()).isPresent())) {
            throw new BadRequestException("Category with this name already exists for the user or as a default.");
        }

        Category category = new Category();
        category.setUserId(userId);
        category.setName(request.getName());
        category.setType(request.getType());
        category.setCreatedAt(Instant.now());
        return categoryRepository.save(category);
    }

    public void deleteCategory(String categoryId, String userId) {
        Category category = getCategoryById(categoryId, userId);
        if (category.getUserId() == null) {
            throw new BadRequestException("Default categories cannot be deleted.");
        }
        if (!expenseRepository.findByUserIdAndCategoryId(userId, categoryId).isEmpty()) {
            throw new BadRequestException("Category cannot be deleted as it is used by existing expenses.");
        }
        categoryRepository.delete(category);
    }
}


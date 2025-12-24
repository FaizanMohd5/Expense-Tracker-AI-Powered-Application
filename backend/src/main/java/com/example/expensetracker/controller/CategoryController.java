package com.example.expensetracker.controller;

import com.example.expensetracker.dto.CategoryRequest;
import com.example.expensetracker.dto.CategoryResponse;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.service.AuthService;
import com.example.expensetracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(Principal principal) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        List<CategoryResponse> categories = categoryService.getUserCategories(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(Principal principal, @RequestBody CategoryRequest request) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        Category newCategory = categoryService.createCategory(userId, request);
        return new ResponseEntity<>(convertToDto(newCategory), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(Principal principal, @PathVariable String id) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        categoryService.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }

    private CategoryResponse convertToDto(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setUserId(category.getUserId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }
}


package com.example.expensetracker.repository;

import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    @Query("{ 'userId': ?0, 'categoryId': ?1 }")
    List<Expense> findByUserIdAndCategoryId(String userId, String categoryId);

    List<Expense> findByUserId(String userId);

    List<Expense> findByUserIdAndType(String userId, Category.CategoryType type);

    List<Expense> findByUserIdAndCategoryIdAndType(String userId, String categoryId, Category.CategoryType type);


}


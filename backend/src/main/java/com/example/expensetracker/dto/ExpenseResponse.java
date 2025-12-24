package com.example.expensetracker.dto;

import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Expense;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class ExpenseResponse {
    private String id;
    private String userId;
    private BigDecimal amount;
    private String categoryId;
    private Category.CategoryType type;
    private Expense.PaymentMethod paymentMethod;
    private LocalDate date;
    private String note;
    private Instant createdAt;
}


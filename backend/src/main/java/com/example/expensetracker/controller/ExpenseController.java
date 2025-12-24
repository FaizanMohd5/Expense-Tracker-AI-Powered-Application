package com.example.expensetracker.controller;

import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.ExpenseResponse;
import com.example.expensetracker.dto.MonthlySummaryResponse;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.AuthService;
import com.example.expensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses(
            Principal principal,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Category.CategoryType type
    ) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        List<ExpenseResponse> expenses = expenseService.getExpenses(userId, month, year, categoryId, type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(Principal principal, @PathVariable String id) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        Expense expense = expenseService.getExpenseById(id, userId);
        return ResponseEntity.ok(convertToDto(expense));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(Principal principal, @RequestBody ExpenseRequest request) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        Expense newExpense = expenseService.createExpense(userId, request);
        return new ResponseEntity<>(convertToDto(newExpense), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(Principal principal, @PathVariable String id, @RequestBody ExpenseRequest request) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        Expense updatedExpense = expenseService.updateExpense(id, userId, request);
        return ResponseEntity.ok(convertToDto(updatedExpense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(Principal principal, @PathVariable String id) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        expenseService.deleteExpense(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            Principal principal,
            @RequestParam int year,
            @RequestParam int month
    ) {
        String userId = authService.getAuthenticatedUser(principal.getName()).getId();
        MonthlySummaryResponse summary = expenseService.getMonthlySummary(userId, year, month);
        return ResponseEntity.ok(summary);
    }

    private ExpenseResponse convertToDto(Expense expense) {
        ExpenseResponse dto = new ExpenseResponse();
        dto.setId(expense.getId());
        dto.setUserId(expense.getUserId());
        dto.setAmount(expense.getAmount());
        dto.setCategoryId(expense.getCategoryId());
        dto.setType(expense.getType());
        dto.setPaymentMethod(expense.getPaymentMethod());
        dto.setDate(expense.getDate());
        dto.setNote(expense.getNote());
        dto.setCreatedAt(expense.getCreatedAt());
        return dto;
    }
}


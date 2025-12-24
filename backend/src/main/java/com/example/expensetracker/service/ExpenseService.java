package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseRequest;
import com.example.expensetracker.dto.MonthlySummaryResponse;
import com.example.expensetracker.exception.BadRequestException;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;

    public List<Expense> getExpenses(String userId, Integer month, Integer year, String categoryId, Category.CategoryType type) {
        if (month != null && year != null) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            if (categoryId != null && type != null) {
                return expenseRepository.findByUserIdAndCategoryIdAndType(userId, categoryId, type);
            } else if (categoryId != null) {
                // This case is not directly covered by a specific repository method, might need adjustment or more generic query.
                // For now, filtering after fetching by month/year.
                return expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate).stream()
                        .filter(expense -> expense.getCategoryId().equals(categoryId))
                        .collect(Collectors.toList());
            } else if (type != null) {
                return expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate).stream()
                        .filter(expense -> expense.getType().equals(type))
                        .collect(Collectors.toList());
            } else {
                return expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
            }
        } else if (categoryId != null && type != null) {
            return expenseRepository.findByUserIdAndCategoryIdAndType(userId, categoryId, type);
        } else if (categoryId != null) {
            return expenseRepository.findByUserIdAndCategoryId(userId, categoryId);
        } else if (type != null) {
            return expenseRepository.findByUserIdAndType(userId, type);
        } else {
            return expenseRepository.findByUserId(userId);
        }
    }

    public Expense getExpenseById(String expenseId, String userId) {
        return expenseRepository.findById(expenseId)
                .filter(expense -> expense.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found or unauthorized access"));
    }

    public Expense createExpense(String userId, ExpenseRequest request) {
        validateExpenseRequest(userId, request);

        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setAmount(request.getAmount());
        expense.setCategoryId(request.getCategoryId());
        expense.setType(request.getType());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setDate(request.getDate());
        expense.setNote(request.getNote());
        expense.setCreatedAt(Instant.now());
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(String expenseId, String userId, ExpenseRequest request) {
        Expense existingExpense = getExpenseById(expenseId, userId);
        validateExpenseRequest(userId, request);

        existingExpense.setAmount(request.getAmount());
        existingExpense.setCategoryId(request.getCategoryId());
        existingExpense.setType(request.getType());
        existingExpense.setPaymentMethod(request.getPaymentMethod());
        existingExpense.setDate(request.getDate());
        existingExpense.setNote(request.getNote());
        return expenseRepository.save(existingExpense);
    }

    public void deleteExpense(String expenseId, String userId) {
        Expense expense = getExpenseById(expenseId, userId);
        expenseRepository.delete(expense);
    }

    public MonthlySummaryResponse getMonthlySummary(String userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        BigDecimal totalIncome = expenses.stream()
                .filter(e -> e.getType() == Category.CategoryType.INCOME)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenses.stream()
                .filter(e -> e.getType() == Category.CategoryType.EXPENSE)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> categoryExpenseSummary = expenses.stream()
                .filter(e -> e.getType() == Category.CategoryType.EXPENSE)
                .collect(Collectors.groupingBy(Expense::getCategoryId,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));

        Map<String, BigDecimal> categoryIncomeSummary = expenses.stream()
                .filter(e -> e.getType() == Category.CategoryType.INCOME)
                .collect(Collectors.groupingBy(Expense::getCategoryId,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));

        MonthlySummaryResponse summary = new MonthlySummaryResponse();
        summary.setYear(year);
        summary.setMonth(month);
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpense(totalExpense);
        summary.setBalance(totalIncome.subtract(totalExpense));
        summary.setCategoryExpenseSummary(categoryExpenseSummary);
        summary.setCategoryIncomeSummary(categoryIncomeSummary);
        return summary;
    }

    private void validateExpenseRequest(String userId, ExpenseRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Expense date cannot be in the future");
        }
        categoryService.getCategoryById(request.getCategoryId(), userId); // Validates category and user access
    }
}


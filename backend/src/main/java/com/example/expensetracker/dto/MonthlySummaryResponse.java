package com.example.expensetracker.dto;

import com.example.expensetracker.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class MonthlySummaryResponse {
    private int year;
    private int month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private Map<String, BigDecimal> categoryExpenseSummary;
    private Map<String, BigDecimal> categoryIncomeSummary;
}


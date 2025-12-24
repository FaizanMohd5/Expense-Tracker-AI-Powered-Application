package com.example.expensetracker.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateUserProfileRequest {
    private String name;
    private String currency;
    private BigDecimal monthlyBudget;
}


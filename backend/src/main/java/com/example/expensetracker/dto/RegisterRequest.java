package com.example.expensetracker.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String currency;
    private BigDecimal monthlyBudget;
}


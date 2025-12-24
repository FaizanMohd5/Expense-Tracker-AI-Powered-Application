package com.example.expensetracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class UserProfileResponse {
    private String id;
    private String name;
    private String email;
    private String currency;
    private BigDecimal monthlyBudget;
    private Instant createdAt;
}


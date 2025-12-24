package com.example.expensetracker.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}


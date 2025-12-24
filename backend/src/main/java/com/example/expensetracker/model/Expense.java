package com.example.expensetracker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Document(collection = "expenses")
@CompoundIndex(name = "userId_date_idx", def = "{'userId': 1, 'date': 1}")
public class Expense {
    @Id
    private String id;

    private String userId;
    private BigDecimal amount;
    private String categoryId;
    private Category.CategoryType type;
    private PaymentMethod paymentMethod;
    private LocalDate date;
    private String note;
    private Instant createdAt;

    public enum PaymentMethod {
        CASH, CARD, UPI, BANK
    }
}


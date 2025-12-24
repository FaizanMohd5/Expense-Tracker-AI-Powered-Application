package com.example.expensetracker.service;

import com.example.expensetracker.dto.RegisterRequest;
import com.example.expensetracker.dto.UpdateUserProfileRequest;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setCurrency(request.getCurrency());
        user.setMonthlyBudget(request.getMonthlyBudget() != null ? request.getMonthlyBudget() : BigDecimal.ZERO);
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User updateProfile(String userId, UpdateUserProfileRequest request) {
        User user = getUserById(userId);
        user.setName(request.getName());
        user.setCurrency(request.getCurrency());
        user.setMonthlyBudget(request.getMonthlyBudget());
        return userRepository.save(user);
    }
}


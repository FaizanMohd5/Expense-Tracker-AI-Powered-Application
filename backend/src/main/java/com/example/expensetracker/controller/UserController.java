package com.example.expensetracker.controller;

import com.example.expensetracker.dto.UpdateUserProfileRequest;
import com.example.expensetracker.dto.UserProfileResponse;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.AuthService;
import com.example.expensetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(Principal principal) {
        User user = authService.getAuthenticatedUser(principal.getName());
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCurrency(user.getCurrency());
        response.setMonthlyBudget(user.getMonthlyBudget());
        response.setCreatedAt(user.getCreatedAt());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(Principal principal, @RequestBody UpdateUserProfileRequest request) {
        User authenticatedUser = authService.getAuthenticatedUser(principal.getName());
        User updatedUser = userService.updateProfile(authenticatedUser.getId(), request);
        UserProfileResponse response = new UserProfileResponse();
        response.setId(updatedUser.getId());
        response.setName(updatedUser.getName());
        response.setEmail(updatedUser.getEmail());
        response.setCurrency(updatedUser.getCurrency());
        response.setMonthlyBudget(updatedUser.getMonthlyBudget());
        response.setCreatedAt(updatedUser.getCreatedAt());
        return ResponseEntity.ok(response);
    }
}


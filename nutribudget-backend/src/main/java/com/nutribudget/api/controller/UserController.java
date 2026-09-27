package com.nutribudget.api.controller;

import com.nutribudget.api.dto.request.UpdateUserRequest;
import com.nutribudget.api.dto.response.ApiResponse;
import com.nutribudget.api.dto.response.UserResponse;
import com.nutribudget.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cap nhat thong tin nguoi dung thanh cong", response));
    }

    // Compatibility endpoint for /api/users/profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Chua dang nhap"));
        }
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
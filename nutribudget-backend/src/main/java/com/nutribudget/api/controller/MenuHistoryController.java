package com.nutribudget.api.controller;

import com.nutribudget.api.dto.response.ApiResponse;
import com.nutribudget.api.dto.response.MenuHistoryResponse;
import com.nutribudget.api.entity.User;
import com.nutribudget.api.repository.UserRepository;
import com.nutribudget.api.service.MenuHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-history")
public class MenuHistoryController {

    @Autowired
    private MenuHistoryService historyService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MenuHistoryResponse>>> getHistoryByUserId(
            @PathVariable Long userId,
            Authentication authentication) {

        Long effectiveUserId = userId;
        if (authentication != null && authentication.getName() != null) {
            User authUser = userRepository.findByEmail(authentication.getName()).orElse(null);
            if (authUser != null) {
                effectiveUserId = authUser.getId();
            }
        }

        List<MenuHistoryResponse> response = historyService.getHistoryByUserId(effectiveUserId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}

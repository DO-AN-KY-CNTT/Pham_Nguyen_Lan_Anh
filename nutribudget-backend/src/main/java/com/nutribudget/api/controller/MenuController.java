package com.nutribudget.api.controller;

import com.nutribudget.api.dto.request.MenuRequest;
import com.nutribudget.api.dto.request.SuggestMenuRequest;
import com.nutribudget.api.dto.request.UpdateMenuItemRequest;
import com.nutribudget.api.dto.response.ApiResponse;
import com.nutribudget.api.dto.response.MenuResponse;
import com.nutribudget.api.dto.response.MenuIngredientsResponse;
import com.nutribudget.api.dto.response.MenuCostResponse;
import com.nutribudget.api.dto.response.SuggestedMenuResponse;
import com.nutribudget.api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMenusByUserId(@PathVariable Long userId) {
        List<MenuResponse> response = menuService.getMenusByUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenuById(@PathVariable Long id) {
        MenuResponse response = menuService.getMenuById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(@RequestBody MenuRequest request) {
        MenuResponse response = menuService.createMenu(request);
        return ResponseEntity.ok(ApiResponse.ok("Tao thuc don thanh cong", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable Long id,
            @RequestBody MenuRequest request) {
        MenuResponse response = menuService.updateMenu(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cap nhat thuc don thanh cong", response));
    }

    @GetMapping("/{menuId}/cost")
    public ResponseEntity<ApiResponse<MenuCostResponse>> getMenuCost(
            @PathVariable Long menuId,
            Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : null;
        MenuCostResponse response = menuService.getMenuCost(menuId, userEmail);
        return ResponseEntity.ok(ApiResponse.ok("Thanh cong", response));
    }

    @PutMapping("/{menuId}/items/{itemId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenuItem(
            @PathVariable Long menuId,
            @PathVariable Long itemId,
            @RequestBody UpdateMenuItemRequest request) {
        MenuResponse response = menuService.updateMenuItem(menuId, itemId, request);
        return ResponseEntity.ok(ApiResponse.ok("Doi mon thanh cong", response));
    }


    @PostMapping("/suggest")
    public ResponseEntity<ApiResponse<List<SuggestedMenuResponse>>> suggestMenus(
            @RequestBody(required = false) SuggestMenuRequest request,
            Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : null;
        List<SuggestedMenuResponse> response = menuService.suggestMenus(request, userEmail);
        return ResponseEntity.ok(ApiResponse.ok("De xuat thuc don thanh cong", response));
    }

    @GetMapping("/suggest")
    public ResponseEntity<ApiResponse<List<SuggestedMenuResponse>>> suggestMenusGet(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer soBua,
            Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : null;
        SuggestMenuRequest request = SuggestMenuRequest.builder()
                .userId(userId)
                .soBua(soBua)
                .build();
        List<SuggestedMenuResponse> response = menuService.suggestMenus(request, userEmail);
        return ResponseEntity.ok(ApiResponse.ok("De xuat thuc don thanh cong", response));
    }

    @GetMapping("/{menuId}/ingredients")
    public ResponseEntity<ApiResponse<MenuIngredientsResponse>> getMenuIngredients(
            @PathVariable Long menuId) {
        MenuIngredientsResponse response = menuService.getMenuIngredients(menuId);
        return ResponseEntity.ok(ApiResponse.ok("Thanh cong", response));
    }
}

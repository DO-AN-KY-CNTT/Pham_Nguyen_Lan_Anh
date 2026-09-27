package com.nutribudget.api.service;

import com.nutribudget.api.dto.request.BudgetRequest;
import com.nutribudget.api.dto.response.BudgetResponse;
import com.nutribudget.api.entity.Budget;
import com.nutribudget.api.entity.User;
import com.nutribudget.api.exception.ResourceNotFoundException;
import com.nutribudget.api.repository.BudgetRepository;
import com.nutribudget.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BudgetResponse> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByNguoiDungId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BudgetResponse createBudget(BudgetRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + request.getUserId()));

        Budget budget = Budget.builder()
                .nguoiDung(user)
                .nganSachNgay(request.getNganSachNgay())
                .nganSachTuan(request.getNganSachTuan())
                .nganSachThang(request.getNganSachThang())
                .build();

        budget = budgetRepository.save(budget);
        return toResponse(budget);
    }

    @Transactional
    public BudgetResponse updateBudget(Long id, BudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay ngan sach voi ID: " + id));

        if (request.getNganSachNgay() != null) budget.setNganSachNgay(request.getNganSachNgay());
        if (request.getNganSachTuan() != null) budget.setNganSachTuan(request.getNganSachTuan());
        if (request.getNganSachThang() != null) budget.setNganSachThang(request.getNganSachThang());

        budget = budgetRepository.save(budget);
        return toResponse(budget);
    }

    private BudgetResponse toResponse(Budget b) {
        return BudgetResponse.builder()
                .id(b.getId())
                .userId(b.getNguoiDung().getId())
                .nganSachNgay(b.getNganSachNgay())
                .nganSachTuan(b.getNganSachTuan())
                .nganSachThang(b.getNganSachThang())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .build();
    }
}
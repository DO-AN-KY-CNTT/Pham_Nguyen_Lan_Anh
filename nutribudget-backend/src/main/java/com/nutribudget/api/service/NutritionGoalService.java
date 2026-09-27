package com.nutribudget.api.service;

import com.nutribudget.api.dto.request.NutritionGoalRequest;
import com.nutribudget.api.dto.response.NutritionGoalResponse;
import com.nutribudget.api.entity.NutritionGoal;
import com.nutribudget.api.entity.User;
import com.nutribudget.api.exception.ResourceNotFoundException;
import com.nutribudget.api.repository.NutritionGoalRepository;
import com.nutribudget.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionGoalService {

    @Autowired
    private NutritionGoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    public List<NutritionGoalResponse> getGoalsByUserId(Long userId) {
        return goalRepository.findByNguoiDungId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NutritionGoalResponse createGoal(NutritionGoalRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + request.getUserId()));

        NutritionGoal goal = NutritionGoal.builder()
                .nguoiDung(user)
                .mucTieu(request.getMucTieu())
                .caloMucTieu(request.getCaloMucTieu())
                .proteinMucTieu(request.getProteinMucTieu())
                .carbMucTieu(request.getCarbMucTieu())
                .fatMucTieu(request.getFatMucTieu())
                .build();

        goal = goalRepository.save(goal);
        return toResponse(goal);
    }

    @Transactional
    public NutritionGoalResponse updateGoal(Long id, NutritionGoalRequest request) {
        NutritionGoal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay muc tieu voi ID: " + id));

        if (request.getMucTieu() != null) goal.setMucTieu(request.getMucTieu());
        if (request.getCaloMucTieu() != null) goal.setCaloMucTieu(request.getCaloMucTieu());
        if (request.getProteinMucTieu() != null) goal.setProteinMucTieu(request.getProteinMucTieu());
        if (request.getCarbMucTieu() != null) goal.setCarbMucTieu(request.getCarbMucTieu());
        if (request.getFatMucTieu() != null) goal.setFatMucTieu(request.getFatMucTieu());

        goal = goalRepository.save(goal);
        return toResponse(goal);
    }

    private NutritionGoalResponse toResponse(NutritionGoal g) {
        return NutritionGoalResponse.builder()
                .id(g.getId())
                .userId(g.getNguoiDung().getId())
                .mucTieu(g.getMucTieu())
                .caloMucTieu(g.getCaloMucTieu())
                .proteinMucTieu(g.getProteinMucTieu())
                .carbMucTieu(g.getCarbMucTieu())
                .fatMucTieu(g.getFatMucTieu())
                .createdAt(g.getCreatedAt())
                .updatedAt(g.getUpdatedAt())
                .build();
    }
}
package com.nutribudget.api.service;

import com.nutribudget.api.dto.request.DishRequest;
import com.nutribudget.api.dto.response.DishResponse;
import com.nutribudget.api.entity.Dish;
import com.nutribudget.api.exception.ResourceNotFoundException;
import com.nutribudget.api.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public List<DishResponse> getAllDishes(String keyword) {
        List<Dish> dishes;
        if (keyword != null && !keyword.trim().isEmpty()) {
            dishes = dishRepository.findByTenMonContainingIgnoreCase(keyword.trim());
        } else {
            dishes = dishRepository.findAll();
        }
        return dishes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public DishResponse getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay mon an voi ID: " + id));
        return toResponse(dish);
    }

    @Transactional
    public DishResponse createDish(DishRequest request) {
        Dish dish = Dish.builder()
                .tenMon(request.getTenMon())
                .moTa(request.getMoTa())
                .hinhAnh(request.getHinhAnh())
                .calo(request.getCalo())
                .protein(request.getProtein())
                .carb(request.getCarb())
                .fat(request.getFat())
                .giaDuKien(request.getGiaDuKien())
                .khauPhan(request.getKhauPhan())
                .build();
        dish = dishRepository.save(dish);
        return toResponse(dish);
    }

    public DishResponse toResponse(Dish d) {
        return DishResponse.builder()
                .id(d.getId())
                .tenMon(d.getTenMon())
                .moTa(d.getMoTa())
                .hinhAnh(d.getHinhAnh())
                .calo(d.getCalo())
                .protein(d.getProtein())
                .carb(d.getCarb())
                .fat(d.getFat())
                .giaDuKien(d.getGiaDuKien())
                .khauPhan(d.getKhauPhan())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
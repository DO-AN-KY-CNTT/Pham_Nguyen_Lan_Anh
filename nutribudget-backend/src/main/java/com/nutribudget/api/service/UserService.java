package com.nutribudget.api.service;

import com.nutribudget.api.dto.request.UpdateUserRequest;
import com.nutribudget.api.dto.response.UserResponse;
import com.nutribudget.api.entity.User;
import com.nutribudget.api.exception.ResourceNotFoundException;
import com.nutribudget.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + id));
        return toResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi email: " + email));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + id));

        if (request.getHoTen() != null) user.setHoTen(request.getHoTen());
        if (request.getSoDienThoai() != null) user.setSoDienThoai(request.getSoDienThoai());
        if (request.getGioiTinh() != null) user.setGioiTinh(request.getGioiTinh());
        if (request.getNgaySinh() != null) user.setNgaySinh(request.getNgaySinh());
        if (request.getChieuCao() != null) user.setChieuCao(request.getChieuCao());
        if (request.getCanNang() != null) user.setCanNang(request.getCanNang());

        user = userRepository.save(user);
        return toResponse(user);
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .hoTen(user.getHoTen())
                .email(user.getEmail())
                .soDienThoai(user.getSoDienThoai())
                .gioiTinh(user.getGioiTinh())
                .ngaySinh(user.getNgaySinh())
                .chieuCao(user.getChieuCao())
                .canNang(user.getCanNang())
                .bmi(user.getBmi())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
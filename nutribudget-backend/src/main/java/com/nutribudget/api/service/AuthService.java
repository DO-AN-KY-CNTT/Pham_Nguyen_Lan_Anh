package com.nutribudget.api.service;

import com.nutribudget.api.dto.request.LoginRequest;
import com.nutribudget.api.dto.request.RegisterRequest;
import com.nutribudget.api.dto.response.AuthResponse;
import com.nutribudget.api.entity.User;
import com.nutribudget.api.exception.ResourceNotFoundException;
import com.nutribudget.api.repository.UserRepository;
import com.nutribudget.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email da duoc dang ky: " + request.getEmail());
        }

        User user = User.builder()
                .hoTen(request.getHoTen())
                .email(request.getEmail())
                .matKhau(passwordEncoder.encode(request.getMatKhau()))
                .soDienThoai(request.getSoDienThoai())
                .gioiTinh(request.getGioiTinh())
                .ngaySinh(request.getNgaySinh())
                .chieuCao(request.getChieuCao())
                .canNang(request.getCanNang())
                .build();

        user = userRepository.save(user);

        String token = tokenProvider.generateTokenFromEmail(user.getEmail());
        return AuthResponse.builder()
                .token(token)
                .user(userService.toResponse(user))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay tai khoan voi email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getMatKhau(), user.getMatKhau())) {
            throw new IllegalArgumentException("Mat khau khong dung");
        }

        String token = tokenProvider.generateTokenFromEmail(user.getEmail());
        return AuthResponse.builder()
                .token(token)
                .user(userService.toResponse(user))
                .build();
    }
}
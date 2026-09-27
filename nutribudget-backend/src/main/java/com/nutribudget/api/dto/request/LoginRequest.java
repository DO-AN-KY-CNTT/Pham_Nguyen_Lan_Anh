package com.nutribudget.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;

    @JsonAlias({"password", "mat_khau"})
    private String matKhau;
}
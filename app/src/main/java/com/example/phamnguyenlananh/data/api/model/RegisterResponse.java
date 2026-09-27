package com.example.phamnguyenlananh.data.api.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private AuthData data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthData getData() {
        return data;
    }

    public void setData(AuthData data) {
        this.data = data;
    }

    public String getToken() {
        return data != null ? data.getToken() : null;
    }

    public UserData getUser() {
        return data != null ? data.getUser() : null;
    }

    public static class AuthData {
        @SerializedName("token")
        private String token;

        @SerializedName("tokenType")
        private String tokenType;

        @SerializedName("user")
        private UserData user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public UserData getUser() {
            return user;
        }

        public void setUser(UserData user) {
            this.user = user;
        }
    }

    public static class UserData {
        @SerializedName("id")
        private Long id;

        @SerializedName("hoTen")
        private String hoTen;

        @SerializedName("email")
        private String email;

        @SerializedName("soDienThoai")
        private String soDienThoai;

        @SerializedName("gioiTinh")
        private String gioiTinh;

        @SerializedName("ngaySinh")
        private String ngaySinh;

        @SerializedName("chieuCao")
        private Double chieuCao;

        @SerializedName("canNang")
        private Double canNang;

        @SerializedName("bmi")
        private Double bmi;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSoDienThoai() {
            return soDienThoai;
        }

        public void setSoDienThoai(String soDienThoai) {
            this.soDienThoai = soDienThoai;
        }

        public String getGioiTinh() {
            return gioiTinh;
        }

        public void setGioiTinh(String gioiTinh) {
            this.gioiTinh = gioiTinh;
        }

        public String getNgaySinh() {
            return ngaySinh;
        }

        public void setNgaySinh(String ngaySinh) {
            this.ngaySinh = ngaySinh;
        }

        public Double getChieuCao() {
            return chieuCao;
        }

        public void setChieuCao(Double chieuCao) {
            this.chieuCao = chieuCao;
        }

        public Double getCanNang() {
            return canNang;
        }

        public void setCanNang(Double canNang) {
            this.canNang = canNang;
        }

        public Double getBmi() {
            return bmi;
        }

        public void setBmi(Double bmi) {
            this.bmi = bmi;
        }
    }
}

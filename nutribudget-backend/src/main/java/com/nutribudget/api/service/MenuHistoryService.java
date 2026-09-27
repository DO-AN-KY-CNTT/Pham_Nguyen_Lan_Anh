package com.nutribudget.api.service;

import com.nutribudget.api.dto.response.MenuHistoryResponse;
import com.nutribudget.api.entity.Menu;
import com.nutribudget.api.entity.MenuHistory;
import com.nutribudget.api.entity.User;
import com.nutribudget.api.repository.MenuHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuHistoryService {

    @Autowired
    private MenuHistoryRepository historyRepository;

    public List<MenuHistoryResponse> getHistoryByUserId(Long userId) {
        return historyRepository.findByNguoiDungIdOrderByThoiGianDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void recordHistory(User user, Menu menu, String hanhDong, String ghiChu) {
        MenuHistory history = MenuHistory.builder()
                .nguoiDung(user)
                .thucDon(menu)
                .hanhDong(hanhDong)
                .ghiChu(ghiChu)
                .build();
        historyRepository.save(history);
    }

    private MenuHistoryResponse toResponse(MenuHistory h) {
        Menu menu = h.getThucDon();
        return MenuHistoryResponse.builder()
                .id(h.getId())
                .userId(h.getNguoiDung() != null ? h.getNguoiDung().getId() : null)
                .thucDonId(menu != null ? menu.getId() : null)
                .tenThucDon(menu != null ? menu.getTenThucDon() : null)
                .ngayApDung(menu != null && menu.getNgayApDung() != null ? menu.getNgayApDung().toString() : null)
                .tongCalo(menu != null ? menu.getTongCalo() : null)
                .tongChiPhi(menu != null ? menu.getTongChiPhi() : null)
                .trangThai(menu != null ? menu.getTrangThai() : null)
                .hanhDong(h.getHanhDong())
                .thoiGian(h.getThoiGian())
                .ghiChu(h.getGhiChu())
                .build();
    }
}
package com.nutribudget.api.repository;

import com.nutribudget.api.entity.MenuHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuHistoryRepository extends JpaRepository<MenuHistory, Long> {
    List<MenuHistory> findByNguoiDungIdOrderByThoiGianDesc(Long userId);
}
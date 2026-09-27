package com.nutribudget.api.repository;

import com.nutribudget.api.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByNguoiDungIdOrderByNgayApDungDesc(Long userId);
    List<Menu> findByNguoiDungIdAndNgayApDung(Long userId, LocalDate ngayApDung);
}
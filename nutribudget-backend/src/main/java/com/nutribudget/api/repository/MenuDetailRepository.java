package com.nutribudget.api.repository;

import com.nutribudget.api.entity.MenuDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDetailRepository extends JpaRepository<MenuDetail, Long> {
    List<MenuDetail> findByThucDonId(Long thucDonId);
}
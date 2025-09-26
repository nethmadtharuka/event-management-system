package com.eventmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eventmanagement.backend.model.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByCategoryIgnoreCase(String category);

    @Query("SELECT v FROM Vendor v WHERE LOWER(v.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(v.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Vendor> searchByKeyword(@Param("keyword") String keyword);
}



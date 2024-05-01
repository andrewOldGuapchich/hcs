package com.andrew.hcsservice.repository;

import com.andrew.hcsservice.model.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocRepository extends JpaRepository<Doc, Long> {
    List<Doc> findByStatus(String status);
}

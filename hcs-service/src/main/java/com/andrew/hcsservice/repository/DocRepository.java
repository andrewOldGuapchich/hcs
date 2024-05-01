package com.andrew.hcsservice.repository;

import com.andrew.hcsservice.model.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {
    List<Doc> findByStatus(String status);
}

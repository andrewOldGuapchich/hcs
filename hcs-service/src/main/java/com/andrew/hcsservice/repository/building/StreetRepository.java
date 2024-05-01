package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
}

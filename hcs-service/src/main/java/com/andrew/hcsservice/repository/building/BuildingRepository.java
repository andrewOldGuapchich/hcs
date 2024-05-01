package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
}

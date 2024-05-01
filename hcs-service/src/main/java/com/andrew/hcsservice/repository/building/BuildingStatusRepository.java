package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.BuildingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingStatusRepository extends JpaRepository<BuildingStatus, Long> {
}

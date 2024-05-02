package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findById(long id);
}

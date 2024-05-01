package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.RoomSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomSpaceRepository extends JpaRepository<RoomSpace, Long> {
}

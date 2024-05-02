package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomSpaceRepository extends JpaRepository<RoomSpace, Long> {
    Optional<RoomSpace> findByNumberAndBuilding(int number, Building building);
    Optional<RoomSpace> findById(Long id);

   /* @Query("select rs from RoomSpace rs " +
            "where rs.oidRoomSpace = :id")
    List<RoomSpace> getOldRoomSpace(@Param("id") Long id);*/

    @Query("select rs from RoomSpace rs " +
            "join rs.building b " +
            "where rs.number = :roomNumber and " +
            "b.number = :buildingNumber and " +
            "rs.amndState = 'A'")
    Optional<RoomSpace> findActive(
            @Param("roomNumber") int number,
            @Param("buildingNumber") int buildingNumber
    );
}

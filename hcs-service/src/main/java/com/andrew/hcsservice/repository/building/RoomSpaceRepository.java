package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.doc.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomSpaceRepository extends JpaRepository<RoomSpace, Long> {

    @Query("select rs from RoomSpace rs " +
            "where rs.building = :building " +
            "and rs.number = :number" +
            " and rs.amndState = 'A'")
    Optional<RoomSpace> findByBuildingAndNumber(@Param("building") Building building,
                                                @Param("number") int number);
    Optional<RoomSpace> findById(Long id);

    @Query("select rs from RoomSpace rs " +
            "where rs.building = :building" +
            " and rs.amndState = 'A'")
    List<RoomSpace> findByBuilding(@Param("building") Building building);

    @Query("select rs from RoomSpace rs " +
            "join rs.building b " +
            "where rs.number = :roomNumber and " +
            "b.number = :buildingNumber and " +
            "rs.amndState = 'A'")
    Optional<RoomSpace> findActive(
            @Param("roomNumber") int number,
            @Param("buildingNumber") int buildingNumber
    );

    @Query("select rs from RoomSpace rs " +
            "inner join OwnerRoom o_r on rs.id = o_r.roomSpace.id " +
            "inner join o_r.owner " +
            "where o_r.owner = :owner")
    List<RoomSpace> findByOwner(@Param("owner") Owner owner);

    @Query(value = "select r from RoomSpace r where r.id in :list")
    List<RoomSpace> findById(@Param("list") List<Long> list);
}

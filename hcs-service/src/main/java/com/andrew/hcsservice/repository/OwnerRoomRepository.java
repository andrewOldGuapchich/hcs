package com.andrew.hcsservice.repository;

import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.OwnerRoom;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OwnerRoomRepository extends JpaRepository<OwnerRoom, Long> {
    @Query("select o_r from OwnerRoom o_r " +
            "where  o_r.owner = :owner and " +
            "o_r.amndState = 'A'")
    List<OwnerRoom> findByOwner(@Param("owner")Owner owner);

    @Query("select o_r from OwnerRoom o_r " +
            "where  o_r.roomSpace = :room and " +
            "o_r.amndState = 'A'")
    List<OwnerRoom> findByRoomSpace(@Param("room")RoomSpace roomSpace);
}

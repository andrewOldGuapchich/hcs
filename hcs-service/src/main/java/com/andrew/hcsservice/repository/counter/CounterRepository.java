package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.counter.Counter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {
    @Query("select c from Counter c " +
            "where c.roomSpace = :roomSpace and " +
            "c.amndState = 'A'")
    List<Counter> counterByRoomSpace(@Param("roomSpace")RoomSpace roomSpace);


    Optional<Counter> findByNumber(String serialNumber);
}

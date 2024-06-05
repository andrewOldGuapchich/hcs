package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.counter.CounterSubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterSubtypeRepository extends JpaRepository<CounterSubType, Long> {
    CounterSubType findByName(String name);
}

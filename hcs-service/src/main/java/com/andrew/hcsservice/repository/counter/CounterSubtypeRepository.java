package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.counter.CounterSubType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterSubtypeRepository extends JpaRepository<CounterSubType, Long> {
}

package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.counter.CounterType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterTypeRepository extends JpaRepository<CounterType, Long> {
}

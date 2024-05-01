package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.counter.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Long> {
}

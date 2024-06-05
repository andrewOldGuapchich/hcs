package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.counter.CounterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterTypeRepository extends JpaRepository<CounterType, Long> {
    CounterType findByName(String name);

}

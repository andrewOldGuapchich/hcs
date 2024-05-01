package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRepository extends JpaRepository<Reading, Long> {
}

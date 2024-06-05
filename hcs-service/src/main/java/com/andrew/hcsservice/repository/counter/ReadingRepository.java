package com.andrew.hcsservice.repository.counter;

import com.andrew.hcsservice.model.entity.Reading;
import com.andrew.hcsservice.model.entity.counter.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {
    @Query("select r from Reading r where " +
            "r.counter = :counter " +
            "and r.period in :periodList")
    List<Reading> findReadingByCounter(@Param("counter")Counter counter,
                                       @Param("periodList") List<String> periodList);
}

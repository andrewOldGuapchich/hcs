package com.andrew.hcsservice.model.entity.counter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "counter_type")
public class CounterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_name")
    private String name;

    @OneToMany(
            mappedBy = "counterType",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Counter> counters;

}

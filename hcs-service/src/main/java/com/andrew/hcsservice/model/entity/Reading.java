package com.andrew.hcsservice.model.entity;

import com.andrew.hcsservice.model.entity.counter.Counter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
@Table(name = "reading")
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "prev_reading_oid")
    @JsonIgnore
    private Reading reading;

    @Column(name = "_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "_value")
    private double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counter_id")
    @JsonIgnore
    private Counter counter;
}

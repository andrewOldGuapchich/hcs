package com.andrew.hcsservice.model.entity.counter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tariff")
@Data
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @JsonIgnore
    private CounterType counterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtype_id")
    @JsonIgnore
    private CounterSubType counterSubType;

    @Column(name = "price")
    private Double price;

    @Column(name = "meas_unit")
    private String measUnit;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JsonIgnore
    private Tariff tariff;
}

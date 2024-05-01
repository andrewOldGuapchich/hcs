package com.andrew.hcsservice.model.entity.counter;

import com.andrew.hcsservice.model.entity.Reading;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "counter")
public class Counter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    @Column(name = "active_status")
    private String activeStatus;

    @Column(name = "serial_number")
    private String number;

    @Column(name = "date_from")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fromDate;

    @Column(name = "date_to")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @JsonIgnore
    private CounterType counterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtype_id")
    @JsonIgnore
    private CounterSubType counterSubType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private RoomSpace roomSpace;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JsonIgnore
    private Counter counter;

    @OneToMany(
            mappedBy = "counter",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Reading> readingList;
}

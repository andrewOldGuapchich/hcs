package com.andrew.hcsservice.model.entity.doc;

import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "update_doc")
@Data
public class UpdateDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    @JsonIgnore
    private Doc doc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private RoomSpace roomSpace;
}

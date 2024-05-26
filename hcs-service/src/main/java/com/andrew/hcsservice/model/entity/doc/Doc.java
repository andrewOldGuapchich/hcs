package com.andrew.hcsservice.model.entity.doc;

import com.andrew.hcsservice.model.entity.OwnerRoom;
import com.andrew.hcsservice.model.entity.counter.Counter;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "doc")
public class Doc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @Column(name = "owner_surname")
    private String ownerSurname;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_patronymic")
    private String ownerPatronymic;

    @JsonFormat(pattern = "aaaa-bbbbbb")
    private String passport;

    @Column(name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Email
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "doc_type")
    private String documentType;

    @JsonIgnore
    @OneToMany(mappedBy = "doc",
            cascade = CascadeType.ALL)
    private List<UpdateDoc> updateDocs;
}

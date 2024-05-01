package com.andrew.hcsservice.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    private String surname;

    @Column(name = "_name")
    private String name;

    private String patronymic;

    @JsonFormat(pattern = "aaaa-bbbbbb")
    private String passport;

    @Email
    private String email;

    @Column(name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "owner_oid")
    @JsonIgnore
    private Owner oidOwner;
}

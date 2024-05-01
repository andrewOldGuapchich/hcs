package com.andrew.hcsservice.model.entity;

import com.andrew.hcsservice.model.entity.status.DocStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

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
}

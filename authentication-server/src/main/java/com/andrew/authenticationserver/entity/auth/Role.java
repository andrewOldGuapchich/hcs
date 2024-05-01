package com.andrew.authenticationserver.entity.auth;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role")
    private String role;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}

package com.andrew.authserver.entity.auth

import jakarta.persistence.*
import lombok.Data

@Entity
@Table(name = "role")
@Data
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 1;
    @Column(name = "role")
    private var role: String? = null;
    @ManyToMany(mappedBy = "roles")
    private var users: List<User>? = null;

    fun getRole(): String? {
        return role;
    }

    fun setRole(role: String) {
        this.role = role;
    }

}
package com.andrew.authserver.entity.auth


import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data

@Entity
@Table(name = "_user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 1

    @Column(name = "username")
    private var username: String? = null

    @Column(name = "email")
    private var email: String? = null

    @Column(name = "password")
    private var password: String? = null

    @Column(name = "ver_code")
    private var code: Int = 0

    @JsonIgnore
    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    @JoinTable(
            name = "user_role",
            joinColumns = [JoinColumn(name = "id_user", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "id_role", referencedColumnName = "id")]
    ) private var roles: List<Role>? = null;

    fun getUsername(): String? {
        return username;
    }

    fun setUsername(newUsername: String) {
        this.username = newUsername;
    }

    fun getEmail(): String? {
        return email;
    }

    fun setEmail(newEmail: String) {
        this.email = newEmail;
    }

    fun getRoles(): List<Role>? {
        return roles;
    }

    fun setRoles(roles: List<Role>){
        this.roles = roles;
    }

    fun getPassword(): String? {
        return password;
    }

    fun setPassword(password: String) {
        this.password = password;
    }

    fun getCode(): Int {
        return code;
    }

    fun setCode(code: Int) {
        this.code = code;
    }
}
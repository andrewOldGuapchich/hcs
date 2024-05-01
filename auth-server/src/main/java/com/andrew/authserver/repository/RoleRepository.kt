package com.andrew.authserver.repository

import com.andrew.authserver.entity.auth.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByRole(role: String): Optional<Role>;
}
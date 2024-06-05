package com.andrew.authenticationserver.repository;

import com.andrew.authenticationserver.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u " +
            "where u.email = :email and u.status ='A'")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("select u from User u " +
            "where u.email = :email and u.status ='W'")
    Optional<User> findWaitingByEmail(@Param("email") String email);
}

package com.andrew.hcsservice.repository;

import com.andrew.hcsservice.model.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query("select o from Owner o " +
            "where o.amndState = 'A' and " +
            "(o.email = :email or " +
            "o.passport = :passport)")
    Optional<Owner> findByEmailAndPassport(@Param("email") String email,
                                    @Param("passport") String passport);

    @Query("select o from Owner o " +
            "where o.amndState = 'A'")
    List<Owner> findAll();

    @Query("select o from Owner o where o.email = :email and o.amndState = 'W'")
    Optional<Owner> findWaitingByEmail(@Param("email") String email);

    @Query("select o from Owner o where o.passport = :passport and o.amndState = 'A'")
    Optional<Owner> findByPassport(@Param("passport") String passport);
}

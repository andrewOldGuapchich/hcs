package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.Street;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
    @Query("select s from Street s " +
            "where s.amndState = 'A'")
    @NotNull List<Street> findAll();
    @NotNull Optional<Street> findById(@NotNull Long idStreet);
    @Query("select distinct s from Street s " +
            "left join fetch s.buildings b")
    List<Street> findStreetWithBuildings(Long idStreet);

    @Query("select s from Street s " +
            "where s.name = :nameStreet " +
            "and s.amndState = 'A'")
    Optional<Street> findStreetByName(@Param("nameStreet") String nameStreet);

    @Query(value = "WITH RECURSIVE street_history AS ("
            + "  SELECT * FROM street WHERE id = :idStreet " +
            "and amnd_state = 'A' "
            + "  UNION ALL "
            + "  SELECT s.* FROM street s "
            + "  JOIN street_history sh ON s.id = sh.street_oid"
            + ") "
            + "SELECT * FROM street_history",
            nativeQuery = true)
    List<Street> findHistoryStreetInfo(@Param("idStreet") Long idStreet);
}

package com.andrew.hcsservice.repository.building;

import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.Street;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findById(long idBuilding);

    @Query("select b from Building b " +
            "where b.amndState = 'A'")
    @NotNull List<Building> findAll();

    @Query("select b from Building b " +
            "where b.street = :street " +
            "and b.amndState = 'A'")
    List<Building> findBuildingByStreet(@Param("street") Street street);

    @Query("select b from Building b " +
            "where b.street = :street" +
            " and b.number = :number " +
            "and b.amndState = 'A'")
    Optional<Building> findBuildingByStreetAndNumber(@Param("street") Street street,
                                                     @Param("number") String numberBuilding);

    @Query(value = "WITH RECURSIVE building_history AS ("
            + "  SELECT * FROM building WHERE id = :idBuilding " +
            "and amnd_state = 'A' "
            + "  UNION ALL "
            + "  SELECT s.* FROM building s "
            + "  JOIN building_history sh ON s.id = sh.building_oid"
            + ") "
            + "SELECT * FROM building_history",
            nativeQuery = true)
    List<Building> findHistoryBuildingInfo(@Param("idBuilding") Long idBuilding);
}

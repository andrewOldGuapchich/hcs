package com.andrew.hcsservice.repository.doc;

import com.andrew.hcsservice.model.entity.doc.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {
    List<Doc> findByStatusAndDocumentType(String status, String type);

    @Query(value = "select d from Doc d where d.id in :list")
    List<Doc> findById(@Param("list") List<Long> list);
}

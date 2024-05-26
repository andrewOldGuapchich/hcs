package com.andrew.hcsservice.repository.doc;

import com.andrew.hcsservice.model.entity.doc.UpdateDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateDocRepository extends JpaRepository<UpdateDoc, Long> {
}

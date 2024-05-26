package com.andrew.hcsservice.service.service_interfaces;

import com.andrew.hcsservice.model.dto.doc.DocDto;
import com.andrew.hcsservice.model.entity.doc.Doc;
import com.andrew.hcsservice.model.entity.Owner;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DocInterfaces {
    ResponseEntity<?> addDocNew(DocDto docDTO);
    void addDocRemove(Owner owner);
    public List<Doc> getWaitingStatus(String type);
    List<Doc> findByIds(List<Long> ids);
    Optional<Doc> findById(Long id);
    void saveDoc(Doc doc);
    void addDocUpdate(Owner owner, List<Long> romListId);
}

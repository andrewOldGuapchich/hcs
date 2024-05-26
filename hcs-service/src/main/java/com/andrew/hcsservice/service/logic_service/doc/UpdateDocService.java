package com.andrew.hcsservice.service.logic_service.doc;

import com.andrew.hcsservice.model.dto.doc.UpdateDocDto;
import com.andrew.hcsservice.model.entity.doc.Doc;
import com.andrew.hcsservice.repository.doc.UpdateDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateDocService {
    private final UpdateDocRepository updateDocRepository;
    //todo доделать потом
    public UpdateDocDto createUpdateDocDto(Doc doc, List<Long> roomListId){
        UpdateDocDto updateDocDto = new UpdateDocDto();
        return updateDocDto;
    }
}

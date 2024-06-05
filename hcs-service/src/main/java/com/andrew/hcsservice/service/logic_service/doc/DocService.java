package com.andrew.hcsservice.service.logic_service.doc;

import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.doc.DocDto;
import com.andrew.hcsservice.model.entity.doc.Doc;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.status.DocStatus;
import com.andrew.hcsservice.model.status.DocType;
import com.andrew.hcsservice.repository.doc.DocRepository;
import com.andrew.hcsservice.service.service_interfaces.DocInterfaces;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocService implements DocInterfaces {
    private final DocRepository docRepository;
    private final UpdateDocService updateDocService;
    private final ModelMapper mapper;

    public ResponseEntity<?> addDocNew(DocDto docDTO){
        /*if(ownerService.isFindOwner(docDTO.getEmail(), docDTO.getPassport())){
            return ResponseEntity.badRequest().body("Такой пользователь уже зарегистрирован!");
        }
        else {*/
            Doc doc = new Doc();
            doc.setOwnerSurname(docDTO.getOwnerName().getOwnerSurname());
            doc.setOwnerName(docDTO.getOwnerName().getOwnerName());
            doc.setOwnerPatronymic(docDTO.getOwnerName().getOwnerPatronymic());
            doc.setPassport(docDTO.getOwnerData().getPassport());
            doc.setBirthDate(docDTO.getOwnerData().getBirthDate());
            doc.setEmail(docDTO.getOwnerInfo().getEmail());
            doc.setPhoneNumber(docDTO.getOwnerInfo().getPhoneNumber());
            doc.setCreateDate(LocalDate.now());
            doc.setStatus(DocStatus.WAITING.getShortName());
            doc.setDocumentType(DocType.NEW.getShortName());
            saveDoc(doc);
            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), doc));
        //}
    }

    public void addDocRemove(Owner owner){
        Doc doc = new Doc();
        doc.setOwnerName(owner.getName());
        doc.setOwnerSurname(owner.getSurname());
        doc.setOwnerPatronymic(owner.getPatronymic());
        doc.setPhoneNumber(owner.getPhoneNumber());
        doc.setEmail(owner.getEmail());
        doc.setBirthDate(owner.getBirthDate());
        doc.setDocumentType(DocType.REMOVE.getShortName());
        doc.setStatus(DocStatus.WAITING.getShortName());
        doc.setCreateDate(LocalDate.now());

        saveDoc(doc);
    }

    //todo доделать потом
    public void addDocUpdate(Owner owner, List<Long> romListId){

    }

    public List<Doc> getWaitingStatus(String type){
        String status = DocStatus.WAITING.getShortName();
        System.out.println(status + type);
        return docRepository.findByStatusAndDocumentType(status, type);
    }

    public List<Doc> findByIds(List<Long> ids){
        return docRepository.findById(ids);
    }

    public Optional<Doc> findById(Long id){
       return docRepository.findById(id);
    }

    public void saveDoc(Doc doc){
        docRepository.save(doc);
    }
}

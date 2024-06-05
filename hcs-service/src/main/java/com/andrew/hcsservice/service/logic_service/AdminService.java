package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.RoomOwnerDto;
import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.building.Street;
import com.andrew.hcsservice.model.entity.doc.Doc;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.status.DocStatus;
import com.andrew.hcsservice.service.addit_service.EmailService;
import com.andrew.hcsservice.service.logic_service.doc.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdminService {
    private DocService docService;
    private OwnerService ownerService;
    private final RoomSpaceService roomSpaceService;
    private final EmailService emailService;
    private final RestTemplate restTemplate;
    private final StreetService streetService;
    private final BuildingService buildingService;
    private final OwnerRoomSpaceService ownerRoomSpaceService;
    @Autowired
    public void setDocService(@Lazy DocService docService) {
        this.docService = docService;
    }

    @Autowired
    public void setOwnerService(@Lazy OwnerService ownerService) {
        this.ownerService = ownerService;
    }
    public ResponseEntity<?> getWaitingStatus(String type){
        return ResponseEntity.ok().body(docService.getWaitingStatus(type));
    }
    public ResponseEntity<?> addRoomForOwner(RoomOwnerDto roomOwnerDto){
        Optional<Owner> optionalOwner = ownerService.findByEmailAndPassport(
                roomOwnerDto.getOwner().getOwnerEmail(),
                roomOwnerDto.getOwner().getOwnerPassport());
        if(optionalOwner.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "User not found"));
        }
        Owner owner = optionalOwner.get();
        Building building = buildingService.findBuildingByStreetAndNumber(roomOwnerDto.getAddress().getStreetName(),
                roomOwnerDto.getAddress().getBuildNumber());
        RoomSpace roomSpace = roomSpaceService.findByBuildingAndNumber(building, roomOwnerDto.getAddress().getRoomNumber());

        ownerRoomSpaceService.addRoomSpaceForOwner(owner, roomSpace);

        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), roomOwnerDto));
    }

    public ResponseEntity<?> sendDocList(List<Long> idDocList){
        List<Doc> resultDocList = docService.findByIds(idDocList);
        LinkedHashMap<Long, String> linkedHashMap = ownerService.mapAndRegisterOwner(resultDocList);
        linkedHashMap.forEach((key, value) -> {
            Doc doc = docService.findById(key).get();
            if (value.equals("Posted")){
                String url = "http://localhost:7170/registration/send-ver-code?email=" + doc.getEmail();
                String htmlContent = "<p>Для прохождения процесса регистрации перейдите по ссылке - " +
                        "<a href=\"" + url + "\">регистрация</a></p>";
                emailService.sendEmail(doc.getEmail(), htmlContent);
            } else{
                String message = "Your application was rejected by the moderators";
                emailService.sendEmail(doc.getEmail(), message);
            }
        });

        return ResponseEntity.ok(
                new ResponseBody<>(HttpStatus.OK.value(), linkedHashMap)
        );
    }

    public ResponseEntity<?> postingDeleteDoc(Long id){
        Optional<Doc> deleteDocOptional = docService.findById(id);
        if(deleteDocOptional.isEmpty()){
            return ResponseEntity.badRequest().body(
                    new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Doc not found!"));
        }

        Doc deleteDoc = deleteDocOptional.get();
        Optional<Owner> deleteOwnerOptional = ownerService.findByEmailAndPassport(deleteDoc.getEmail(), deleteDoc.getPassport());
        if(deleteOwnerOptional.isEmpty()){
            return ResponseEntity.badRequest().body(
                    new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Owner not found!"));
        }


        Owner deleteOwner = deleteOwnerOptional.get();
        ownerService.deleteOwner(deleteOwner.getId());

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:7170/user/close-user/" + deleteOwner.getEmail())
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate
                .exchange(url, HttpMethod.POST, entity, Object.class);

        deleteDoc.setStatus(DocStatus.POSTED.getShortName());
        docService.saveDoc(deleteDoc);

        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), deleteDoc));
    }



    public ResponseEntity<?> findAllOwner(){
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), ownerService.findAll()));
    }

    public ResponseEntity<?> findAllOwnerInfo(){
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), ownerService.findAllWithRoomSpaces()));
    }
}

package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.AppException;
import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.doc.IdDto;
import com.andrew.hcsservice.model.dto.doc.UpdateDocDto;
import com.andrew.hcsservice.model.dto.owner.InfoOwnerDto;
import com.andrew.hcsservice.model.dto.owner.OwnerDto;
import com.andrew.hcsservice.model.entity.doc.Doc;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.OwnerRoom;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.status.AmndStatus;
import com.andrew.hcsservice.model.status.DocStatus;
import com.andrew.hcsservice.repository.doc.DocRepository;
import com.andrew.hcsservice.repository.OwnerRepository;
import com.andrew.hcsservice.service.service_interfaces.DocInterfaces;
import com.andrew.hcsservice.service.service_interfaces.OwnerInterfaces;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OwnerService implements OwnerInterfaces {
    private final OwnerRepository ownerRepository;
    private final DocRepository docRepository;
    private DocInterfaces docServiceInterfaces;
    private final OwnerRoomSpaceService ownerRoomSpaceService;
    private final RoomSpaceService roomSpaceService;

    private final ModelMapper mapper;

    //редактирование пользовательских данных. редактирование недвижимости производится админом системы
    public ResponseEntity<?> updateOwnerInfo(OwnerDto updateOwnerDto, Long ownerId){
        try{
            Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
            Owner oldOwner = validateAndGetOwner(optionalOwner);
            oldOwner.setAmndState(AmndStatus.INACTIVE.getShortName());
            oldOwner.setAmndDate(LocalDate.now());
            oldOwner.setId(ownerId);

            Owner newOwner = mapper.map(updateOwnerDto, Owner.class);
            newOwner.setAmndDate(LocalDate.now());
            newOwner.setAmndState(AmndStatus.ACTIVE.getShortName());
            newOwner.setOidOwner(oldOwner);
            newOwner.setOwnerRoomList(oldOwner.getOwnerRoomList());

            ownerRepository.save(oldOwner);
            ownerRepository.save(newOwner);

            List<OwnerRoom> ownerRoomList = ownerRoomSpaceService.findByOwner(oldOwner);
            for(OwnerRoom ownerRoom : ownerRoomList){
                ownerRoom.setOwner(newOwner);
            }

            ownerRoomSpaceService.saveAll(ownerRoomList);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), newOwner));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public ResponseEntity<?> createDeleteDoc(Long ownerId){
        try{
            Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
            Owner deleteOwner = validateAndGetOwner(optionalOwner);
            docServiceInterfaces.addDocRemove(deleteOwner);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), deleteOwner));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    //todo доделать потом
    public ResponseEntity<?> createUpdateDoc(Long ownerId, IdDto idDto){
        try{
            Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
            Owner owner = validateAndGetOwner(optionalOwner);
            docServiceInterfaces.addDocUpdate(owner, idDto.getIdList());
            List<RoomSpace> roomSpaceList = roomSpaceService.findListById(idDto.getIdList());

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), owner));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public void deleteOwner(Long id) {
        try{
            Optional<Owner> optionalOwner = ownerRepository.findById(id);
            Owner deleteOwner = validateAndGetOwner(optionalOwner);
            List<OwnerRoom> ownerRoomList = ownerRoomSpaceService.findByOwner(deleteOwner);
            deleteOwner.setAmndState(AmndStatus.INACTIVE.getShortName());
            deleteOwner.setAmndDate(LocalDate.now());
            deleteOwner.setOwnerRoomList(null);
            ownerRoomList.forEach(ownerRoom -> ownerRoom.setRoomSpace(null));

            ownerRepository.save(deleteOwner);
            ownerRoomSpaceService.saveAll(ownerRoomList);

            Owner addOwner = new Owner();
            addOwner.setName(deleteOwner.getName());
            addOwner.setSurname(deleteOwner.getSurname());
            addOwner.setPatronymic(deleteOwner.getPatronymic());
            addOwner.setBirthDate(deleteOwner.getBirthDate());
            addOwner.setEmail(deleteOwner.getEmail());
            addOwner.setPassport(deleteOwner.getPassport());
            addOwner.setPhoneNumber(deleteOwner.getPhoneNumber());
            addOwner.setOidOwner(deleteOwner);
            addOwner.setOwnerRoomList(ownerRoomList);
            addOwner.setAmndDate(LocalDate.now());
            addOwner.setAmndState(AmndStatus.CLOSE.getShortName());

            ownerRepository.save(addOwner);
            //return ResponseEntity.ok()
                  //  .body(new ResponseBody<>(HttpStatus.OK.value(), addOwner));
        } catch (AppException appException){
            //return ResponseEntity.badRequest()
                    //.body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public ResponseEntity<?> findOwnerInfo(Long idOwner){
       try{
           Optional<Owner> optionalOwner = ownerRepository.findById(idOwner);
           Owner owner = validateAndGetOwner(optionalOwner);

           return ResponseEntity.ok()
                   .body(new ResponseBody<>(HttpStatus.OK.value(), mapOwnerOnInfoDto(owner)));
       } catch (AppException appException){
           return ResponseEntity.badRequest()
                   .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
       }
    }

    public ResponseEntity<?> findOwnerInfo(String email, String passport){
        try{
            Optional<Owner> optionalOwner = findByPassportAndEmail(email, passport);
            Owner owner = validateAndGetOwner(optionalOwner);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), mapOwnerOnInfoDto(owner)));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public List<Owner> findAll(){
        List<Owner> owners = ownerRepository.findAll();
        return ownerRepository.findAll();
    }

    public List<InfoOwnerDto> findAllWithRoomSpaces(){
        List<Owner> owners = ownerRepository.findAll();
        List<InfoOwnerDto> infoOwnerList = new ArrayList<>();

        owners.forEach(owner -> {
            infoOwnerList.add(mapOwnerOnInfoDto(owner));
        });
        return infoOwnerList;
    }

    public Optional<Owner> findByPassportAndEmail(String email, String passport){
        return ownerRepository.findByEmailAndPassport(email, passport);
    }

    public LinkedHashMap<Long, String> mapAndRegisterOwner(List<Doc> docList){
        List<Owner> addOwnerList = new ArrayList<>();
        LinkedHashMap<Long, String> mapStatusRegister = new LinkedHashMap<>();
        for (Doc value : docList) {
            Owner owner = mapDocOnOwner(value);
            if (!isFindOwner(owner.getEmail(), owner.getPassport()))
                mapStatusRegister.put(value.getId(), "Rejected");
            else {
                addOwnerList.add(owner);
                owner.setAmndState(AmndStatus.WAITING.getShortName());
                mapStatusRegister.put(value.getId(), "Posted");
            }
            value.setStatus(DocStatus.POSTED.getShortName());
        }
        docRepository.saveAll(docList);
        ownerRepository.saveAll(addOwnerList);

        return mapStatusRegister;
    }

    public ResponseEntity<?> activateOwner(String email){
        try{
            Optional<Owner> optionalOwner = ownerRepository.findWaitingByEmail(email);
            Owner activateOwner = validateAndGetOwner(optionalOwner);
            activateOwner.setAmndState(AmndStatus.ACTIVE.getShortName());

            ownerRepository.save(activateOwner);
            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), activateOwner));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    private Owner validateAndGetOwner(Optional<Owner> optionalOwner){
        if(optionalOwner.isEmpty()){
            throw new AppException("Owner not found!");
        }
        Owner owner = optionalOwner.get();
        if(!(owner.getAmndState().equals(AmndStatus.ACTIVE.getShortName()) ||
                owner.getAmndState().equals(AmndStatus.WAITING.getShortName()))){
            throw new AppException("Incorrect id!");
        }

        return owner;
    }

    public boolean isFindOwner(String email, String passport){
        return ownerRepository.findByEmailAndPassport(email, passport).isEmpty();
    }

    private Owner mapDocOnOwner(Doc doc){
        Owner owner = new Owner();
        owner.setSurname(doc.getOwnerSurname());
        owner.setName(doc.getOwnerName());
        owner.setPatronymic(doc.getOwnerPatronymic());
        owner.setPassport(doc.getPassport());
        owner.setBirthDate(doc.getBirthDate());
        owner.setEmail(doc.getEmail());
        owner.setAmndDate(LocalDate.now());
        owner.setAmndState(AmndStatus.INACTIVE.getShortName());

        return owner;
    }

    private InfoOwnerDto mapOwnerOnInfoDto(Owner owner){
        InfoOwnerDto dto = new InfoOwnerDto();
        List<RoomSpace> roomSpaceList = roomSpaceService.findRoomSpacesByOwner(owner);
        dto.setOwnerSurname(owner.getSurname());
        dto.setOwnerName(owner.getName());
        dto.setOwnerPatronymic(owner.getPatronymic());
        dto.setPassport(owner.getPassport());
        dto.setBirthDate(owner.getBirthDate());
        dto.setEmail(owner.getEmail());
        dto.setPhoneNumber(owner.getPhoneNumber());
        dto.setRoomSpaceList(roomSpaceList);

        return dto;
    }
}

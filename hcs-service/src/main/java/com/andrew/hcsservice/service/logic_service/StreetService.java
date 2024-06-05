package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.AppException;
import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.street.CreateStreetDto;
import com.andrew.hcsservice.model.dto.street.InfoStreetDto;
import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.Street;
import com.andrew.hcsservice.model.status.AmndStatus;
import com.andrew.hcsservice.repository.building.BuildingRepository;
import com.andrew.hcsservice.repository.building.StreetRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StreetService {
    private final StreetRepository streetRepository;
    private final ModelMapper mapper;

    public ResponseEntity<?> createStreet(CreateStreetDto createStreetDto) {
        Optional<Street> streetOptional =
                streetRepository.findStreetByName(createStreetDto.getName());
        if (streetOptional.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "The street already exists!"));
        }

        Street newStreet = mapper.map(createStreetDto, Street.class);
        newStreet.setAmndDate(LocalDate.now());
        newStreet.setAmndState(AmndStatus.ACTIVE.getShortName());

        streetRepository.save(newStreet);
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), newStreet));
    }

    @Transactional
    public ResponseEntity<?> updateStreet(CreateStreetDto updateStreetDto, String name){
        try{
            Optional<Street> streetOptional =
                    streetRepository.findStreetByName(name);
            Street currentStreet = validateAndGetStreet(streetOptional);
            Street oldStreet = new Street();

            oldStreet.setName(currentStreet.getName());
            oldStreet.setAmndDate(currentStreet.getAmndDate());
            oldStreet.setAmndState(AmndStatus.INACTIVE.getShortName());
            oldStreet.setCountBuilding(currentStreet.getCountBuilding());
            oldStreet.setOidStreet(currentStreet.getOidStreet());
            streetRepository.save(oldStreet);

            currentStreet.setName(updateStreetDto.getName());
            currentStreet.setCountBuilding(updateStreetDto.getCountBuilding());
            currentStreet.setAmndDate(LocalDate.now());
            currentStreet.setOidStreet(oldStreet);
            streetRepository.save(currentStreet);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), currentStreet));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteStreet(String name){
        try{
            Optional<Street> streetOptional =
                    streetRepository.findStreetByName(name);
            Street currentStreet = validateAndGetStreet(streetOptional);
            Street oldStreet = new Street();

            oldStreet.setName(currentStreet.getName());
            oldStreet.setAmndDate(currentStreet.getAmndDate());
            oldStreet.setAmndState(AmndStatus.INACTIVE.getShortName());
            oldStreet.setCountBuilding(currentStreet.getCountBuilding());
            oldStreet.setOidStreet(currentStreet.getOidStreet());
            streetRepository.save(oldStreet);

            currentStreet.setAmndState(AmndStatus.CLOSE.getShortName());
            currentStreet.setAmndDate(LocalDate.now());
            currentStreet.setOidStreet(oldStreet);
            streetRepository.save(currentStreet);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), currentStreet));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }


    public ResponseEntity<?> getAll(){
        List<Street> streetList = streetRepository.findAll();
        List<InfoStreetDto> infoStreetDtoList =
                mapper.map(streetList, new TypeToken<List<InfoStreetDto>>() {}.getType());
        return ResponseEntity.ok(infoStreetDtoList);
    }

    public ResponseEntity<?> getStreetInfo(String nameStreet){
        try{
            Optional<Street> streetOptional = streetRepository.findStreetByName(nameStreet);
            Street street = validateAndGetStreet(streetOptional);
            InfoStreetDto streetDto = mapper.map(street, InfoStreetDto.class);
            return ResponseEntity.ok(streetDto);
        } catch (AppException appException) {
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public Street findByName(String name){
        Optional<Street> streetOptional = streetRepository.findStreetByName(name);
        return validateAndGetStreet(streetOptional);
    }
    public ResponseEntity<?> getHistoryInfo(Long idStreet) {
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(),
                        streetRepository.findHistoryStreetInfo(idStreet)));
    }
    private Street validateAndGetStreet(Optional<Street> streetOptional) {
        if (streetOptional.isEmpty()) {
            throw new AppException("Street not found!");
        }
        Street street = streetOptional.get();
        if (!street.getAmndState().equals(AmndStatus.ACTIVE.getShortName())) {
            throw new AppException("Street not found! Incorrect name!");
        }
        return street;
    }

    private Street getNewStreet(Street oldStreet){
        Street newStreet = new Street();
        newStreet.setName(oldStreet.getName());
        newStreet.setAmndDate(LocalDate.now());
        newStreet.setCountBuilding(oldStreet.getCountBuilding());

        List<Building> newBuildingList = new ArrayList<>();
        for(Building building : oldStreet.getBuildings()){
            building.setStreet(newStreet);
            newBuildingList.add(building);
        }

        newStreet.setBuildings(newBuildingList);
        newStreet.setOidStreet(oldStreet);
        return newStreet;
    }
}

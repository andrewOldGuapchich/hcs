package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.AppException;
import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.AddReadingDto;
import com.andrew.hcsservice.model.dto.CounterDto;
import com.andrew.hcsservice.model.dto.CreateCounterDto;
import com.andrew.hcsservice.model.dto.roomspace.AddressRoomDto;
import com.andrew.hcsservice.model.dto.roomspace.RoomSpaceCreateDto;
import com.andrew.hcsservice.model.dto.roomspace.RoomSpaceReadingDto;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.counter.Counter;
import com.andrew.hcsservice.model.status.AmndStatus;
import com.andrew.hcsservice.model.status.RoomSpaceStatus;
import com.andrew.hcsservice.repository.building.RoomSpaceRepository;
import lombok.RequiredArgsConstructor;
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
public class RoomSpaceService {
    private final RoomSpaceRepository roomSpaceRepository;
    private final BuildingService buildingService;
    private final CounterService counterService;

    @Transactional
    public ResponseEntity<?> addNewRoomSpace(RoomSpaceCreateDto roomSpaceCreateDto){
        System.out.println();
        Building building = buildingService.findBuildingByStreetAndNumber(
                roomSpaceCreateDto.getAddress().getStreetName(),
                roomSpaceCreateDto.getAddress().getBuildNumber()
        );
        if(building == null) {
            return ResponseEntity.badRequest().body("Не найден дом!");
        }
        else if (roomSpaceRepository.findByBuildingAndNumber(
                building, roomSpaceCreateDto.getAddress().getNumber()).isPresent()){
            return ResponseEntity.badRequest().body("В этом доме уже зарегистрирована квартира с таким номером!");
        } else {
            RoomSpace roomSpace = mapperToRoomSpace(roomSpaceCreateDto);
            roomSpace.setBuilding(building);

            roomSpaceRepository.save(roomSpace);
            return ResponseEntity.ok(roomSpace);
        }
    }

    @Transactional
    public ResponseEntity<?> updateRoomSpace(RoomSpaceCreateDto roomSpaceCreateDto) {
        try{
            Building building = buildingService.findBuildingByStreetAndNumber(
                    roomSpaceCreateDto.getAddress().getStreetName(),
                    roomSpaceCreateDto.getAddress().getBuildNumber()
            );
            if (building == null) {
                return ResponseEntity.badRequest()
                        .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Не найден дом!"));
            }

            RoomSpace oldRoomSpace = validateAndGetRoomSpace(building, roomSpaceCreateDto.getAddress().getNumber());
            oldRoomSpace.setAmndState(AmndStatus.INACTIVE.getShortName());

            RoomSpace newRoomSpace = mapperToRoomSpace(roomSpaceCreateDto);
            newRoomSpace.setAmndState(AmndStatus.ACTIVE.getShortName());
            newRoomSpace.setOidRoomSpace(oldRoomSpace);
            newRoomSpace.setBuilding(building);

            roomSpaceRepository.save(oldRoomSpace);
            roomSpaceRepository.save(newRoomSpace);
            return ResponseEntity
                    .ok(new ResponseBody<>(HttpStatus.OK.value(), newRoomSpace));

        } catch (AppException e){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteRoomSpace(AddressRoomDto dto) {
        try{
            Building building = buildingService.findBuildingByStreetAndNumber(dto.getStreetName(), dto.getBuildNumber());
            RoomSpace oldRoomSpace = validateAndGetRoomSpace(building, dto.getRoomNumber());
            oldRoomSpace.setAmndState(AmndStatus.INACTIVE.getShortName());
            //oldRoomSpace.setId(idRoomSpace);
            roomSpaceRepository.save(oldRoomSpace);

            RoomSpace newRoomSpace = mapperToRoomSpace(oldRoomSpace);
            newRoomSpace.setAmndState(AmndStatus.CLOSE.getShortName());
            roomSpaceRepository.save(newRoomSpace);

            return ResponseEntity
                    .ok(new ResponseBody<>(HttpStatus.OK.value(), newRoomSpace));
        } catch (AppException e){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public RoomSpace findByBuildingAndNumber(Building building, int number){
        return validateAndGetRoomSpace(building, number);
    }

    public ResponseEntity<?> infoRoomSpaceWithCounter(AddressRoomDto addressRoomDto){
        try{
            Building building = buildingService.findBuildingByStreetAndNumber(addressRoomDto.getStreetName(), addressRoomDto.getBuildNumber());
            RoomSpace roomSpace = validateAndGetRoomSpace(building, addressRoomDto.getRoomNumber());
            List<Counter> counterList = counterService.findCounterByRoomSpace(roomSpace);
            RoomSpaceReadingDto roomSpaceReadingDto = mapperToRoomSpacesDto(roomSpace, counterList);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), roomSpaceReadingDto));
        } catch (AppException e){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    /*public RoomSpace findById(Long roomId){
        return validateAndGetRoomSpace(roomId);
    }*/

    public List<RoomSpace> findRoomSpacesByOwner(Owner owner){
        return roomSpaceRepository.findByOwner(owner);
    }

    public List<RoomSpace> findListById(List<Long> ids){
        return roomSpaceRepository.findById(ids);
    }

    public ResponseEntity<?> addCounterForRoomSpace(CreateCounterDto counterDto){
        List<Counter> counters = counterService.registerCounter(counterDto);
        counters.forEach(item -> {
            Building building = buildingService
                    .findBuildingByStreetAndNumber(
                            counterDto.getAddress().getStreet(),
                            counterDto.getAddress().getBuilding()
                    );

            RoomSpace roomSpace = validateAndGetRoomSpace(building, counterDto.getAddress().getRoomSpace());

            item.setRoomSpace(roomSpace);
        });

        counterService.saveAll(counters);

        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), counters));
    }


    private RoomSpaceReadingDto mapperToRoomSpacesDto(RoomSpace roomSpace, List<Counter> counters){
        RoomSpaceReadingDto roomSpaceReadingDto = new RoomSpaceReadingDto();
        List<CounterDto> counterDto = new ArrayList<>();
        counters.forEach(counter -> {
            CounterDto dto = new CounterDto();
            dto.setSerialNumber(counter.getNumber());
            dto.setTypeDate(new CounterDto.TypeData(
                    counter.getCounterType().getName(),
                    counter.getCounterSubType().getName()
            ));

            counterDto.add(dto);
        });

        roomSpaceReadingDto.setStatus(RoomSpaceStatus.fromShortName(roomSpace.getStatus()));

        roomSpaceReadingDto.setStreetBuilding(new RoomSpaceReadingDto.StreetBuilding(
                roomSpace.getBuilding().getStreet().getName(),
                roomSpace.getBuilding().getNumber(),
                roomSpace.getNumber()
        ));
        roomSpaceReadingDto.setArea(new RoomSpaceReadingDto.Area(
                roomSpace.getTotalArea(),
                roomSpace.getLivingArea()
        ));
        roomSpaceReadingDto.setCounters(counterDto);

        return roomSpaceReadingDto;
    }


    private RoomSpace mapperToRoomSpace(RoomSpace roomSpace){
        RoomSpace newRoomSpace = new RoomSpace();
        newRoomSpace.setNumber(roomSpace.getNumber());
        newRoomSpace.setBuilding(roomSpace.getBuilding());
        newRoomSpace.setAmndDate(LocalDate.now());
        newRoomSpace.setTotalArea(roomSpace.getTotalArea());
        newRoomSpace.setLivingArea(roomSpace.getLivingArea());
        newRoomSpace.setStatus(roomSpace.getStatus());
        newRoomSpace.setOidRoomSpace(roomSpace);
        return newRoomSpace;
    }

    private RoomSpace mapperToRoomSpace(RoomSpaceCreateDto roomSpaceCreateDto){
        RoomSpace roomSpace = new RoomSpace();
        roomSpace.setNumber(roomSpaceCreateDto.getAddress().getNumber());
        roomSpace.setTotalArea(roomSpaceCreateDto.getArea().getTotalArea());
        roomSpace.setLivingArea(roomSpaceCreateDto.getArea().getLivingArea());
        roomSpace.setAmndDate(LocalDate.now());
        roomSpace.setAmndState(AmndStatus.ACTIVE.getShortName());
        roomSpace.setStatus(roomSpaceCreateDto.getStatus().getShortName());
        return roomSpace;
    }

    private RoomSpace validateAndGetRoomSpace(Building building, int number) {
        Optional<RoomSpace> roomSpaceOptional = roomSpaceRepository.findByBuildingAndNumber(building, number);
        if (roomSpaceOptional.isEmpty()) {
            throw new AppException("Помещение не найдено!");
        }
        RoomSpace roomSpace = roomSpaceOptional.get();
        if (!roomSpace.getAmndState().equals(AmndStatus.ACTIVE.getShortName())) {
            throw new AppException("Помещение не найдено! Неверный id!");
        }
        return roomSpace;
    }
}

package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.AppException;
import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.*;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.counter.Counter;
import com.andrew.hcsservice.model.entity.counter.CounterSubType;
import com.andrew.hcsservice.model.entity.counter.CounterType;
import com.andrew.hcsservice.model.status.AmndStatus;
import com.andrew.hcsservice.repository.counter.CounterRepository;
import com.andrew.hcsservice.repository.counter.CounterSubtypeRepository;
import com.andrew.hcsservice.repository.counter.CounterTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CounterService {
    private final CounterRepository counterRepository;
    private final CounterTypeRepository counterTypeRepository;
    private final CounterSubtypeRepository counterSubtypeRepository;
    private final ReadingService readingService;

    public List<Counter> registerCounter(CreateCounterDto counterDtoList){
        List<Counter> counters = new ArrayList<>();
        counterDtoList.getTypeCounters().forEach(item -> {
            String number = generateSerialNumber();
            while (counterRepository.findByNumber(number).isPresent()){
                number = generateSerialNumber();
            }
            CounterType counterType = counterTypeRepository
                    .findByName(item.getType());
            CounterSubType counterSubType = counterSubtypeRepository
                    .findByName(item.getSubtype());
            Counter counter = new Counter();

            counter.setAmndDate(LocalDate.now());
            counter.setAmndState(AmndStatus.ACTIVE.getShortName());
            counter.setActiveStatus("Y");
            counter.setNumber(number);
            counter.setFromDate(item.getDate().getDateFrom());
            counter.setToDate(item.getDate().getDateTo());
            counter.setCounterType(counterType);
            counter.setCounterSubType(counterSubType);

            counters.add(counter);
        });
        return counters;
    }

    public void saveAll(List<Counter> counters){
        counterRepository.saveAll(counters);
    }

    public ResponseEntity<?> getCounterType (){
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), counterTypeRepository.findAll()));
    }
    public ResponseEntity<?> getCounterSubType (){
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), counterSubtypeRepository.findAll()));
    }


    public ResponseEntity<?> sendReadingForCounter(AddReadingDto addReadingDto){
        try{
            Counter counter = validateAndGetCounter(addReadingDto.getSerialNumber());
            readingService.addReading(addReadingDto, counter);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), addReadingDto));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }



    public ResponseEntity<?> infoCounterWithReadings(String serialNumber, int countMonth){
        try {
            Counter counter = validateAndGetCounter(serialNumber);
            CounterDto dto = new CounterDto();
            List<ReadingInfoDto> readings = readingService.getListReadings(counter, countMonth);
            dto.setSerialNumber(counter.getNumber());
            dto.setTypeDate(new CounterDto.TypeData(
                    counter.getCounterType().getName(),
                    counter.getCounterSubType().getName()
            ));
            CounterReadingDto counterReadingDto = new CounterReadingDto();
            counterReadingDto.setCounterDto(dto);
            counterReadingDto.setReadings(readings);

            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), counterReadingDto));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public List<Counter> findCounterByRoomSpace(RoomSpace roomSpace){
        return counterRepository.counterByRoomSpace(roomSpace);
    }

    private String generateSerialNumber(){
        String number = "";
        Random random = new Random();
        long part1 = random.nextLong(((999_999_999 - 1_000_000_00)) + 1) + 1_000_000_00;
        long part2 = random.nextLong(((9999999999L - 1_000_000_000L)) + 1L) + 1_000_000_00L;
        number = String.valueOf(part1) + String.valueOf(part2);

        return number;
    }

    private Counter validateAndGetCounter(String serialNumber) {
        Optional<Counter> counterOptional = counterRepository.findByNumber(serialNumber);
        if (counterOptional.isEmpty()) {
            throw new AppException("Счетчик не найден!");
        }
        Counter counter = counterOptional.get();
        System.out.println(counter.getNumber() + " " + counter.getAmndState());
        if (!counter.getAmndState().equals(AmndStatus.ACTIVE.getShortName())) {
            throw new AppException("Счетчик не найден! Неверный серийный номер!");
        }
        return counter;
    }
}

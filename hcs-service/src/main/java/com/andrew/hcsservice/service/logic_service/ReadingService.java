package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.AddReadingDto;
import com.andrew.hcsservice.model.dto.ReadingInfoDto;
import com.andrew.hcsservice.model.entity.Reading;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.counter.Counter;
import com.andrew.hcsservice.repository.counter.ReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReadingService {
    private final ReadingRepository readingRepository;

    public void addReading(AddReadingDto addReadingDto, Counter counter) {
        Reading reading = new Reading();
        reading.setCounter(counter);
        reading.setDate(LocalDate.now());
        reading.setValue(addReadingDto.getValue());
        reading.setPeriod(addReadingDto.getPeriod());

        readingRepository.save(reading);
    }

    public List<ReadingInfoDto> getListReadings(Counter counter, int count){
        List<String> periodList = monthAndYearList(count);
        return mapToReadingDto(readingRepository.findReadingByCounter(counter, periodList));
    }

    private List<ReadingInfoDto> mapToReadingDto(List<Reading> readings){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<ReadingInfoDto> list = new ArrayList<>();
        readings.forEach(reading -> {
            ReadingInfoDto readingInfoDto = new ReadingInfoDto();
            readingInfoDto.setDate(reading.getDate().format(formatter));
            readingInfoDto.setValue(reading.getValue());
            readingInfoDto.setPeriod(reading.getPeriod());

            list.add(readingInfoDto);
        });
        return list;
    }

    private List<String> monthAndYearList(int count){
        List<String> list = new ArrayList<>();
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "Январь");
        monthMap.put(2, "Февраль");
        monthMap.put(3, "Март");
        monthMap.put(4, "Апрель");
        monthMap.put(5, "Май");
        monthMap.put(6, "Июнь");
        monthMap.put(7, "Июль");
        monthMap.put(8, "Август");
        monthMap.put(9, "Сентябрь");
        monthMap.put(10, "Октябрь");
        monthMap.put(11, "Ноябрь");
        monthMap.put(12, "Декабрь");

        int currentMonthNumber = LocalDate.now().getMonthValue();
        int currentYearNumber = LocalDate.now().getYear();

        for(int i = 0; i < count; i++){
            list.add(monthMap.get(currentMonthNumber) + " " + currentYearNumber);
            currentMonthNumber--;
            if(currentMonthNumber == 0) {
                currentMonthNumber = 12;
                currentYearNumber--;
            }
        }

        System.out.println(list);
        return list;
    }
}

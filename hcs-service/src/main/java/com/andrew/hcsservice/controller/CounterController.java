package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.AddReadingDto;
import com.andrew.hcsservice.service.logic_service.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counter")
@RequiredArgsConstructor
public class CounterController {
    private final CounterService counterService;

    @GetMapping("/info/{serial-number}")
    public ResponseEntity<?> info(@PathVariable("serial-number")String serialNumber,
                                  @RequestParam("count") int count){
        return counterService.infoCounterWithReadings(serialNumber, count);
    }

    @PostMapping("/add-reading")
    public ResponseEntity<?> sendReading(@RequestBody AddReadingDto addReadingDto){
        return counterService.sendReadingForCounter(addReadingDto);
    }

    @GetMapping("/counter-type")
    public ResponseEntity<?> getCounterType(){
        return counterService.getCounterType();
    }

    @GetMapping("/counter-subtype")
    public ResponseEntity<?> getCounterSubType(){
        return counterService.getCounterSubType();
    }
}

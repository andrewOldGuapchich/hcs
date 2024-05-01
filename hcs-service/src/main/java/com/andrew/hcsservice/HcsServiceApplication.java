package com.andrew.hcsservice;

import com.andrew.hcsservice.model.entity.status.DocStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringBootApplication
public class HcsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HcsServiceApplication.class, args);

        System.out.println(DocStatus.valueOf("W"));
    }

}

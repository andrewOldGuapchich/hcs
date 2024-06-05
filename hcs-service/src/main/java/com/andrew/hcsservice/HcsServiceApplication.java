package com.andrew.hcsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class HcsServiceApplication {

    public static void main(String[] args) {
        //send();
        SpringApplication.run(HcsServiceApplication.class, args);
    }


    /*@Scheduled(cron = "*///30 * * * * ?")
    public static void send(){
        System.out.println("hello");
    }

}

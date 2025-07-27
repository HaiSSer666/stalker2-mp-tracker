package com.stalker2tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Stalker2MpTrackerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(Stalker2MpTrackerApplication.class, args);
    }
}
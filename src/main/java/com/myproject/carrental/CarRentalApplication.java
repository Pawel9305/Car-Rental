package com.myproject.carrental;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableVaadin
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }

}

package com.myproject.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RentalDto {
    private long id;
    private User user;
    private Car car;
    private LocalDate from;
    private LocalDate to;
    private String location;
    private BigDecimal totalCost;
}



package com.myproject.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class RentalRequest {
    long userId;
    long carId;
    LocalDate from;
    LocalDate to;
    String location;
    List<Long> optionalEquipmentIds;
}

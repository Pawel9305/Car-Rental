package com.myproject.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AdditionalEquipmentDto {
    private long id;
    private BigDecimal price;
    private String description;
}

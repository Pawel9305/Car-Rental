package com.myproject.carrental.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Data
@AllArgsConstructor
public class CarDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;
    @JsonProperty("type")
    private String type;

    @JsonProperty("tankCapacity")
    private int tankCapacity;

    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("location")
    private String location;
    @JsonIgnore
    private List<RentalDto> rentals;

    public CarDto(String brand, String model, String type, int tankCapacity, BigDecimal price, String location, List<RentalDto> rentals) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.tankCapacity = tankCapacity;
        this.price = price;
        this.location = location;
        this.rentals = rentals;
    }

    @JsonCreator
    public CarDto(long id, String brand, String model, String type, int tankCapacity, BigDecimal price, String location) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.tankCapacity = tankCapacity;
        this.price = price;
        this.location = location;
    }
}

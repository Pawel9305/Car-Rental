package com.myproject.carrental.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Cars")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "id", unique = true)
    private long id;

    @Column(name = "Brand")
    private String brand;

    @Column(name = "Model")
    private String model;

    @Column(name = "Type")
    private String type;

    @Column(name = "Tank_capacity")
    private int tankCapacity;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "Location")
    private String location;

    @OneToMany(
            targetEntity = Rental.class,
            mappedBy = "car",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonBackReference
    private List<Rental> rentals;

    @Column(name = "available")
    private boolean available = false;

    public Car(long id, String brand, String model, String type, int tankCapacity, BigDecimal price, String location) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.tankCapacity = tankCapacity;
        this.price = price;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Car{" +
                "rentals=" + rentals +
                '}';
    }
}

package com.myproject.carrental.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Rental")
public class Rental {

    public Rental(User user, Car car, LocalDate from, LocalDate to, String location, BigDecimal totalCost) {
        this.user = user;
        this.car = car;
        this.from = from;
        this.to = to;
        this.location = location;
        this.totalCost = totalCost;
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "date_from")
    private LocalDate from;

    @Column(name = "date_to")
    private LocalDate to;

    @Column(name = "location")
    @NotNull
    private String location;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "returned")
    private boolean returned = false;
}

package com.myproject.carrental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "Additional_equipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdditionalEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    @Column(name = "eq_description")
    @NotNull
    private String description;

    public AdditionalEquipment(BigDecimal price, String description) {
        this.price = price;
        this.description = description;
    }
}

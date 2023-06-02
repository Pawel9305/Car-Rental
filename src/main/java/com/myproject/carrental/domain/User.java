package com.myproject.carrental.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Users")
public class User {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "surname")
    private String surname;

    @NotNull
    @Column(name = "idNumber")
    private String idNumber;

    @OneToMany(targetEntity = Rental.class,
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Rental> rentals;

    @Column(name = "email")
    private String email;

    public User(String name, String surname, String idNumber, List<Rental> rentals, String email) {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.rentals = rentals;
        this.email = email;
    }
}

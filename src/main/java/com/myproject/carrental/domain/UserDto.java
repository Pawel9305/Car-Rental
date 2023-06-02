package com.myproject.carrental.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;


@Getter
@Data
@AllArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private String surname;
    private String idNumber;
    private String email;

    @JsonIgnore
    private List<RentalDto> rentals;

    @JsonCreator
    public UserDto(String name, String surname, String idNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.email = email;
    }
}

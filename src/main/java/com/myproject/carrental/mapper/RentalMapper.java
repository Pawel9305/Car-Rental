package com.myproject.carrental.mapper;

import com.myproject.carrental.domain.Rental;
import com.myproject.carrental.domain.RentalDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class RentalMapper {

    public RentalDto mapToRentalDto(Rental rental) {
        return new RentalDto(
                rental.getId(),
                rental.getUser(),
                rental.getCar(),
                rental.getFrom(),
                rental.getTo(),
                rental.getLocation(),
                rental.getTotalCost()
        );
    }

    public Rental mapToRental(RentalDto rentalDto) {
        return new Rental(
                rentalDto.getId(),
                rentalDto.getUser(),
                rentalDto.getCar(),
                rentalDto.getFrom(),
                rentalDto.getTo(),
                rentalDto.getLocation(),
                rentalDto.getTotalCost(),
                false
        );
    }

    public List<RentalDto> mapToRentalDtoList(List<Rental> rentals) {
        return rentals.stream()
                .map(this::mapToRentalDto)
                .toList();
    }
}

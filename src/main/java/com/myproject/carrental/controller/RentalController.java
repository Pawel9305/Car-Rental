package com.myproject.carrental.controller;


import com.myproject.carrental.domain.RentalDto;
import com.myproject.carrental.domain.RentalRequest;
import com.myproject.carrental.exception.CarNotFoundException;
import com.myproject.carrental.exception.RentalNotFoundException;
import com.myproject.carrental.exception.RentalOverlappingException;
import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.service.RentalService;
import com.myproject.carrental.service.facade.RentalFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("v1/rental")
@RequiredArgsConstructor
public class RentalController {
    private final RentalFacade rentalFacade;

    private final RentalService rentalService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> rentACar(@RequestBody RentalRequest request) throws UserNotFoundException, CarNotFoundException, RentalOverlappingException {
        rentalFacade.rentACar(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "totalcost", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> totalRentalCost(@RequestBody RentalRequest request) {
        return ResponseEntity.ok(rentalFacade.calculateRental(request));
    }

    @GetMapping(value = "cost", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> calculateBasicCost(@RequestBody RentalRequest request) {
        BigDecimal basicCost = rentalService.calculateBasicCost(request);
        return ResponseEntity.ok(basicCost);
    }

    @GetMapping("return/{rentalId}")
    public ResponseEntity<Boolean> returnACar(@PathVariable("rentalId") final long rentalId) throws CarNotFoundException, RentalNotFoundException {
        return ResponseEntity.ok(rentalService.returnACar(rentalId));
    }

    @GetMapping(value = "check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkOverlap(@RequestBody RentalRequest request) {
        boolean isRented = rentalService.isOverlap(request);
        return ResponseEntity.ok(isRented);
    }

    @GetMapping(value = "equipment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> checkAdditionalCosts(@RequestBody RentalRequest request) {
        return ResponseEntity.ok(rentalService.additionalCosts(request));
    }

    @GetMapping(value = "car/{carId}")
    public ResponseEntity<List<RentalDto>> checkRentalsForCar(@PathVariable("carId") long carId) throws CarNotFoundException {
        return ResponseEntity.ok(rentalService.getRentalsForCar(carId));
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity<List<RentalDto>> checkRentalsForUser(@PathVariable("userId") long userId) throws UserNotFoundException {
        return ResponseEntity.ok(rentalService.getRentalsForUser(userId));
    }
}

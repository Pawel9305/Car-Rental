package com.myproject.carrental.controller;

import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("v1/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping(value = "addCar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addACar(@RequestBody CarDto carDto) {
        carService.addCar(carDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteACar(@PathVariable("id") long id) {
        carService.removeACar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping(value = "brand/{brand}")
    public ResponseEntity<List<CarDto>> getCarsByBrand(@PathVariable("brand") final String brand) {
        return ResponseEntity.ok(carService.getCarsByBrand(brand));
    }

    @GetMapping(value = "type/{type}")
    public ResponseEntity<List<CarDto>> getCarsByType(@PathVariable("type") final String type) {
        return ResponseEntity.ok(carService.getCarsByType(type));
    }

    @GetMapping(value = "location/{location}")
    public ResponseEntity<List<CarDto>> getCarsByLocation(@PathVariable("location") final String location) {
        return ResponseEntity.ok(carService.getCarsByLocation(location));
    }

    @GetMapping(value = "price/{maxValue}")
    public ResponseEntity<List<CarDto>> carsByPriceRange(@PathVariable("maxValue") final BigDecimal maxValue) {
        return ResponseEntity.ok(carService.getCarsPriceRangeUpTo(maxValue));
    }
}

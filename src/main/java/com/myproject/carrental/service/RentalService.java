package com.myproject.carrental.service;

import com.myproject.carrental.domain.*;
import com.myproject.carrental.exception.CarNotFoundException;
import com.myproject.carrental.exception.RentalNotFoundException;
import com.myproject.carrental.exception.RentalOverlappingException;
import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.mapper.CarMapper;
import com.myproject.carrental.mapper.RentalMapper;
import com.myproject.carrental.mapper.UserMapper;
import com.myproject.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserService userService;
    private final CarService carService;
    private final RentalCalculator calculator;
    private final UserMapper userMapper;
    private final RentalMapper rentalMapper;
    private final CarMapper carMapper;

    public void rent(RentalRequest request) throws UserNotFoundException, CarNotFoundException, RentalOverlappingException {
        if (!isOverlap(request)) {
            Car car = carMapper.mapToCar(carService.getById(request.getCarId()));
            carService.changeCarsAvailability(car.getId(), false);
            User user = userMapper.mapToUser(userService.getById(request.getUserId()));
            BigDecimal basicCost = calculator.calculate(request.getCarId(), request.getFrom(), request.getTo());
            BigDecimal additionalCost = calculator.additionalCosts(request.getOptionalEquipmentIds());
            BigDecimal totalCost = basicCost.add(additionalCost);
            Rental rental = new Rental(user, car, request.getFrom(), request.getTo(),
                    request.getLocation(), totalCost);
            rentalRepository.save(rental);
        } else {
            throw new RentalOverlappingException();
        }
    }

    public boolean returnACar(long rentalId) throws CarNotFoundException, RentalNotFoundException {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);
        rental.setReturned(true);
        rentalRepository.save(rental);
        carService.changeCarsAvailability(rental.getCar().getId(), true);
        Rental rentalToCheck = rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);
        return rentalToCheck.isReturned();
    }

    public BigDecimal calculateBasicCost(RentalRequest request) {
        return calculator.calculate(request.getCarId(), request.getFrom(), request.getTo());
    }

    public boolean isOverlap(RentalRequest request) {
        return calculator.isOverlap(request.getCarId(), request.getFrom(), request.getTo());
    }

    public BigDecimal additionalCosts(RentalRequest request) {
        return calculator.additionalCosts(request.getOptionalEquipmentIds());
    }

    public List<RentalDto> getRentalsForCar(long carId) throws CarNotFoundException {
        return carService.getById(carId).getRentals();
    }

    public List<RentalDto> getRentalsForUser(long userId) throws UserNotFoundException {
        return userService.getById(userId).getRentals();
    }

    public List<RentalDto> getAllRentals() {
        return rentalMapper.mapToRentalDtoList(rentalRepository.findAll());
    }
}

package com.myproject.carrental.service;

import com.myproject.carrental.domain.AdditionalEquipment;
import com.myproject.carrental.repository.CarRepository;
import com.myproject.carrental.repository.EquipmentRepository;
import com.myproject.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalCalculator {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;

    public BigDecimal calculate(long carId, LocalDate from, LocalDate to) {
        BigDecimal price = carRepository.findById(carId).getPrice();
        long rentPeriod = Duration.between(from.atStartOfDay(), to.atStartOfDay()).toDays();
        return price.multiply(BigDecimal.valueOf(rentPeriod));
    }

    public boolean isOverlap(long carId, LocalDate from, LocalDate to) {
        return rentalRepository.findAll().stream()
                .filter(rental -> rental.getCar().getId() == carId)
                .anyMatch(rental -> !rental.getTo().isBefore(from) && !rental.getFrom().isAfter(to));
    }

    public BigDecimal additionalCosts(List<Long> equipment) {
        return equipmentRepository.findAll().stream()
                .filter(eq -> equipment.contains(eq.getId()))
                .map(AdditionalEquipment::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

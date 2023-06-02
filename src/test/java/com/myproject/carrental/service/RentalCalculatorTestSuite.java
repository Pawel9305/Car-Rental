package com.myproject.carrental.service;

import com.myproject.carrental.domain.AdditionalEquipment;
import com.myproject.carrental.domain.Car;
import com.myproject.carrental.domain.Rental;
import com.myproject.carrental.domain.User;
import com.myproject.carrental.repository.CarRepository;
import com.myproject.carrental.repository.EquipmentRepository;
import com.myproject.carrental.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RentalCalculatorTestSuite {

    @Autowired
    private RentalCalculator rentalCalculator;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private EquipmentRepository equipmentRepository;

    @MockBean
    private RentalRepository rentalRepository;

    private final long CAR_ID = 1L;

    @Test
    void testRentalCalculatorCalculate() {
        //Given
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location");
        when(carRepository.findById(CAR_ID)).thenReturn(car);

        //When
        BigDecimal resultCost = rentalCalculator.calculate(CAR_ID, LocalDate.now(), LocalDate.now().plusDays(5));

        //Then
        verify(carRepository, times(1)).findById(CAR_ID);
        assertEquals(new BigDecimal("500"), resultCost);
    }

    @Test
    void testRentalCalculatorIsOverlapReturnsTrue() {
        //Given
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of(), true);
        Rental rental = new Rental(new User(), car, LocalDate.now(), LocalDate.now().plusDays(5),
                "Test_location", new BigDecimal("400"));

        List<Rental> rentals = List.of(rental);
        when(rentalRepository.findAll()).thenReturn(rentals);

        //When
        boolean result = rentalCalculator.isOverlap(CAR_ID, LocalDate.now().plusDays(2), LocalDate.now().plusDays(10));

        //Then
        assertTrue(result);
        verify(rentalRepository, times(1)).findAll();
    }

    @Test
    void testRentalCalculatorIsOverlapReturnsFalse() {
        //Given
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of(), true);
        Rental rental = new Rental(new User(), car, LocalDate.of(2023, 6, 10), LocalDate.of(2023, 7, 15),
                "Test_location", new BigDecimal("400"));
        List<Rental> rentals = List.of(rental);
        when(rentalRepository.findAll()).thenReturn(rentals);

        //When
        boolean result = rentalCalculator.isOverlap(CAR_ID, LocalDate.now(), LocalDate.of(2023, 6, 9));

        //Then
        assertFalse(result);
        verify(rentalRepository, times(1)).findAll();
    }

    @Test
    void testRentalCalculatorAdditionalCosts() {
        //Given
        List<AdditionalEquipment> additionalEquipments = List.of(new AdditionalEquipment(1L, new BigDecimal("130"), "child_seat"),
                new AdditionalEquipment(2L, new BigDecimal("150"), "additional_insurance"));
        when(equipmentRepository.findAll()).thenReturn(additionalEquipments);
        List<Long> equipmentIds = List.of(1L, 2L);

        //When
        BigDecimal result = rentalCalculator.additionalCosts(equipmentIds);

        //Then
        assertEquals(new BigDecimal("280"), result);
        verify(equipmentRepository, times(1)).findAll();
    }

    @Test
    void testRentalCalculatorAdditionalCostsNoCosts() {
        //Given


        //When


        //Then
    }
}

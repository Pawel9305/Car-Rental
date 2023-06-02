package com.myproject.carrental.service.facade;

import com.myproject.carrental.domain.Car;
import com.myproject.carrental.domain.RentalRequest;
import com.myproject.carrental.repository.CarRepository;
import com.myproject.carrental.service.RentalService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RentalFacadeTestSuite {

    @Autowired
    private RentalFacade rentalFacade;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private RentalService rentalService;

    private final long CAR_ID = 1L;
    private final long USER_ID = 1L;

    @Test
    void testCalculateRentalSuccessful() {
        //Given
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of(), true);
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.now().plusDays(4),
                "Warsaw", List.of(2L, 3L));
        when(rentalService.calculateBasicCost(request)).thenReturn(new BigDecimal("400"));
        when(rentalService.additionalCosts(request)).thenReturn(new BigDecimal("230"));
        when(carRepository.findById(CAR_ID)).thenReturn(car);

        //When
        BigDecimal result = rentalFacade.calculateRental(request);

        //Then
        assertEquals(new BigDecimal("630"), result);
    }
}

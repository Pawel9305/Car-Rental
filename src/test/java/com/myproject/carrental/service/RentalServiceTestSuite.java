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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RentalServiceTestSuite {

    @Autowired
    private RentalService rentalService;

    @MockBean
    private RentalRepository rentalRepository;

    @MockBean
    private RentalMapper rentalMapper;

    @MockBean
    private CarMapper carMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private CarService carService;

    @MockBean
    private UserService userService;

    @MockBean
    private RentalCalculator rentalCalculator;

    private final long CAR_ID = 1L;
    private final long USER_ID = 1L;

    private final long RENTAL_ID = 1L;

    @Test
    void testRentalServiceRent() throws UserNotFoundException, CarNotFoundException, RentalOverlappingException {
        //Given
        RentalRequest request = new RentalRequest(1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "test_location", List.of());

        Car car = new Car(CAR_ID, "Test_brand", "Test_model", "Test_type", 70, new BigDecimal("200"),
                "Test_location", List.of(), true);
        CarDto carDto = new CarDto("Test_brand", "Test_model", "Test_type", 70, new BigDecimal("200"),
                "Test_location", List.of());
        User user = new User(USER_ID, "Test_name", "Test_surname", "12345678", List.of(), "test@email.com");
        UserDto userDto = new UserDto(USER_ID, "Test_name", "Test_surname", "12345678", "test@email.com", List.of());
        Rental rental = new Rental(user, car, request.getFrom(), request.getTo(), request.getLocation(), new BigDecimal("400"));
        when(carService.getById(CAR_ID)).thenReturn(carDto);
        when(carMapper.mapToCar(carDto)).thenReturn(car);
        when(userMapper.mapToUser(userDto)).thenReturn(user);
        when(userService.getById(USER_ID)).thenReturn(userDto);
        when(rentalCalculator.calculate(car.getId(), request.getFrom(), request.getTo())).thenReturn(rental.getTotalCost());
        when(rentalCalculator.additionalCosts(any())).thenReturn(BigDecimal.ZERO);

        //When
        rentalService.rent(request);

        //Then
        verify(rentalRepository, times(1)).save(rental);
        verify(carMapper, times(1)).mapToCar(carDto);
        verify(userMapper, times(1)).mapToUser(userDto);
        verify(userService, times(1)).getById(USER_ID);
        verify(rentalCalculator, times(1)).calculate(car.getId(), request.getFrom(), request.getTo());
        verify(rentalCalculator, times(1)).additionalCosts(any());
    }

    @Test
    void testRentalServiceReturnACar() throws CarNotFoundException, RentalNotFoundException {
        //Given
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 80, new BigDecimal("200"), "Warsaw", List.of(), false);
        Rental rental = new Rental(RENTAL_ID, new User(), car, LocalDate.now(), LocalDate.now().plusDays(2), "Warsaw", new BigDecimal("400"), false);
        when(rentalRepository.findById(RENTAL_ID)).thenReturn(Optional.of(rental));

        //When
        boolean result = rentalService.returnACar(RENTAL_ID);

        //Then
        assertTrue(result);
        verify(rentalRepository, times(2)).findById(RENTAL_ID);
        verify(carService, times(1)).changeCarsAvailability(CAR_ID, true);
    }

    @Test
    void testRentalServiceCalculateBasicCost() {
        //Given
        BigDecimal totalCost = new BigDecimal("200");
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.now().plusDays(2), "test_location", List.of());
        when(rentalCalculator.calculate(request.getCarId(), request.getFrom(), request.getTo())).thenReturn(totalCost);

        //When
        BigDecimal result = rentalService.calculateBasicCost(request);

        //Then
        assertEquals(totalCost, result);
        verify(rentalCalculator, times(1)).calculate(request.getCarId(), request.getFrom(), request.getTo());
    }

    @Test
    void testRentalServiceIsOverlapReturnsTrue() {
        //Given
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.now().plusDays(2), "test_location", List.of());
        when(rentalCalculator.isOverlap(request.getCarId(), request.getFrom(), request.getTo())).thenReturn(true);

        //When
        boolean result = rentalService.isOverlap(request);

        //Then
        assertTrue(result);
        verify(rentalCalculator, times(1)).isOverlap(request.getCarId(), request.getFrom(), request.getTo());
    }

    @Test
    void testRentalServiceIsOverlapReturnsFalse() {
        //Given
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.now().plusDays(2), "test_location", List.of());
        when(rentalCalculator.isOverlap(request.getCarId(), request.getFrom(), request.getTo())).thenReturn(false);

        //When
        boolean result = rentalService.isOverlap(request);

        //Then
        assertFalse(result);
        verify(rentalCalculator, times(1)).isOverlap(request.getCarId(), request.getFrom(), request.getTo());
    }

    @Test
    void testRentalServiceAdditionalCosts() {
        //Given
        List<AdditionalEquipment> equipmentList = List.of(new AdditionalEquipment(1L, new BigDecimal("130"), "child_seat"));
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.now().plusDays(2), "test_location", List.of(1L));
        when(rentalCalculator.additionalCosts(request.getOptionalEquipmentIds())).thenReturn(equipmentList.get(0).getPrice());

        //When
        BigDecimal result = rentalService.additionalCosts(request);

        //Then
        assertEquals(equipmentList.get(0).getPrice(), result);
        verify(rentalCalculator, times(1)).additionalCosts(any());
    }

    @Test
    void testRentalServiceGetRentalsForCar() throws CarNotFoundException {
        //Given
        CarDto carDto = new CarDto("Test_brand", "Test_model", "Test_type", 70, new BigDecimal("200"),
                "Test_location", List.of(new RentalDto(RENTAL_ID, new User(), new Car(), LocalDate.now(), LocalDate.now().plusDays(2),
                "test_location", new BigDecimal("200"))));
        when(carService.getById(CAR_ID)).thenReturn(carDto);

        //When
        List<RentalDto> resultList = rentalService.getRentalsForCar(CAR_ID);

        //Then
        assertEquals(1, resultList.size());
        assertEquals(RENTAL_ID, resultList.get(0).getId());
    }

    @Test
    void testRentalServiceGetRentalsForCarEmptyList() throws CarNotFoundException {
        //Given
        CarDto carDto = new CarDto("Test_brand", "Test_model", "Test_type", 70, new BigDecimal("200"),
                "Test_location", List.of());
        when(carService.getById(CAR_ID)).thenReturn(carDto);

        //When
        List<RentalDto> resultList = rentalService.getRentalsForCar(CAR_ID);

        //Then
        assertEquals(0, resultList.size());
    }

    @Test
    void testRentalServiceGetRentalsForUser() throws UserNotFoundException {
        //Given
        UserDto userDto = new UserDto(USER_ID, "Test_name", "Test_surname", "12345678", "test@email.com",
                List.of(new RentalDto(RENTAL_ID, new User(), new Car(), LocalDate.now(), LocalDate.now().plusDays(2),
                        "test_location", new BigDecimal("200"))));
        when(userService.getById(RENTAL_ID)).thenReturn(userDto);

        //When
        List<RentalDto> resultList = rentalService.getRentalsForUser(RENTAL_ID);

        //Then
        assertEquals(1, resultList.size());
        assertEquals(RENTAL_ID, resultList.get(0).getId());
    }

    @Test
    void testRentalServiceGetRentalsForUserEmptyList() throws UserNotFoundException {
        //Given
        UserDto userDto = new UserDto(USER_ID, "Test_name", "Test_surname", "12345678", "test@email.com",
                List.of());
        when(userService.getById(RENTAL_ID)).thenReturn(userDto);

        //When
        List<RentalDto> resultList = rentalService.getRentalsForUser(RENTAL_ID);

        //Then
        assertEquals(0, resultList.size());
    }
}

package com.myproject.carrental.service;

import com.myproject.carrental.domain.Car;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.exception.CarNotFoundException;
import com.myproject.carrental.mapper.CarMapper;
import com.myproject.carrental.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CarServiceTestSuite {

    @Autowired
    private CarService carService;

    @MockBean
    private CarMapper carMapper;

    @MockBean
    private CarRepository carRepository;

    private final long CAR_ID = 1L;

    @Test
    void testCarDbServiceAddCar() {
        //Given
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location");
        CarDto carDto = new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of());
        when(carMapper.mapToCar(carDto)).thenReturn(car);

        //When
        carService.addCar(carDto);

        //Then
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testCarDbServiceRemoveACar() {
        //Given
        //When
        carService.removeACar(CAR_ID);
        //Then
        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCarDbServiceGetAllCarsEmptyList() {
        //Given
        when(carRepository.findAll()).thenReturn(List.of());
        when(carMapper.mapToCarDtoList(anyList())).thenReturn(List.of());

        //When
        List<CarDto> cars = carService.getAllCars();

        //Then
        verify(carRepository, times(1)).findAll();
        assertEquals(0, cars.size());
    }

    @Test
    void testCarDbServiceGetAllCars() {
        //Given
        List<Car> cars = List.of(new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location"));
        List<CarDto> carDtos = List.of(new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of()));
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        //When
        List<CarDto> resultList = carService.getAllCars();

        //Then
        verify(carRepository, times(1)).findAll();
        assertEquals(CAR_ID, resultList.get(0).getId());
        assertEquals(1, resultList.size());
    }

    @Test
    void testCarDbServiceGetCarsByBrand() {
        //Given
        List<Car> cars = List.of(new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location"));
        List<CarDto> carDtos = List.of(new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of()));
        when(carRepository.findByBrand(any())).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        //When
        List<CarDto> resultList = carService.getCarsByBrand("test_brand");

        //Then
        verify(carRepository, times(1)).findByBrand("test_brand");
        assertEquals(CAR_ID, resultList.get(0).getId());
        assertEquals("test_brand", resultList.get(0).getBrand());
    }

    @Test
    void testCarDbServiceGetCarsByBrandEmptyList() {
        //Given
        when(carRepository.findByBrand(any())).thenReturn(List.of());

        //When
        List<CarDto> resultList = carService.getCarsByBrand("test_brand");

        //Then
        verify(carRepository, times(1)).findByBrand("test_brand");
        assertEquals(0, resultList.size());
    }

    @Test
    void testCarDbServiceGetCarsByLocation() {
        //Given
        List<Car> cars = List.of(new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location"));
        List<CarDto> carDtos = List.of(new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of()));
        when(carRepository.findByLocation(any())).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        //When
        List<CarDto> resultList = carService.getCarsByLocation("test_location");

        //Then
        verify(carRepository, times(1)).findByLocation("test_location");
        assertEquals(CAR_ID, resultList.get(0).getId());
        assertEquals("test_location", resultList.get(0).getLocation());
    }

    @Test
    void testCarDbServiceGetCarsByLocationEmptyList() {
        //Given
        when(carRepository.findByLocation(any())).thenReturn(List.of());

        //When
        List<CarDto> resultList = carService.getCarsByLocation("test_location");

        //Then
        verify(carRepository, times(1)).findByLocation("test_location");
        assertEquals(0, resultList.size());
    }

    @Test
    void testCarDbServiceGetCarsByType() {
        //Given
        List<Car> cars = List.of(new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location"));
        List<CarDto> carDtos = List.of(new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of()));
        when(carRepository.findByType(any())).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        //When
        List<CarDto> resultList = carService.getCarsByType("test_type");

        //Then
        verify(carRepository, times(1)).findByType("test_type");
        assertEquals(CAR_ID, resultList.get(0).getId());
        assertEquals("test_type", resultList.get(0).getType());
    }

    @Test
    void testCarDbServiceGetCarsByTypeEmptyList() {
        //Given
        when(carRepository.findByType(any())).thenReturn(List.of());

        //When
        List<CarDto> resultList = carService.getCarsByType("test_type");

        //Then
        verify(carRepository, times(1)).findByType("test_type");
        assertEquals(0, resultList.size());
    }

    @Test
    void testCarDbServiceGetCarsPriceRangeUpTo() {
        //Given
        BigDecimal maxPrice = new BigDecimal("110");
        List<Car> cars = List.of(new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location"));
        List<CarDto> carDtos = List.of(new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of()));
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        //When
        List<CarDto> resultList = carService.getCarsPriceRangeUpTo(maxPrice);

        //Then
        verify(carRepository, times(1)).findAll();
        assertEquals(1, resultList.size());
        assertEquals(CAR_ID, resultList.get(0).getId());
    }

    @Test
    void testCarDbServiceGetCarsPriceRangeUpToEmptyList() {
        BigDecimal maxPrice = new BigDecimal("90");
        List<Car> cars = List.of(new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location"));
        List<CarDto> carDtos = List.of(new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of()));
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.mapToCarDtoList(cars)).thenReturn(carDtos);

        //When
        List<CarDto> resultList = carService.getCarsPriceRangeUpTo(maxPrice);

        //Then
        verify(carRepository, times(1)).findAll();
        assertEquals(0, resultList.size());
    }

    @Test
    void testCarDbServiceGetCarsById() throws CarNotFoundException {
        //Given
        Long carId = 1L;
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location");
        CarDto carDto = new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of());
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.mapToCarDto(car)).thenReturn(carDto);

        //When
        CarDto resultCar = carService.getById(carId);

        //Then
        verify(carRepository, times(1)).findById(carId);
        assertEquals(CAR_ID, resultCar.getId());
    }

    @Test
    void testCarDbServiceGetCarsByIdNoCarFound() {
        //Given
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location");
        CarDto carDto = new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of());
        when(carRepository.findById(CAR_ID)).thenReturn(car);
        when(carMapper.mapToCarDto(car)).thenReturn(carDto);

        //When&Then
        assertThrows(CarNotFoundException.class, () -> carService.getById(2));
    }

    @Test
    void testCarDbServiceChangeCarsAvailability() throws CarNotFoundException {
        //Given
        Long carId = 1L;
        Car car = new Car(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of(), true);
        CarDto carDto = new CarDto(CAR_ID, "test_brand", "test_model", "test_type", 50, new BigDecimal("100"), "test_location", List.of());
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.mapToCarDto(car)).thenReturn(carDto);

        //When
        carService.changeCarsAvailability(carId, false);

        //Then
        assertFalse(carRepository.findById(carId).get().isAvailable());
        verify(carRepository, times(2)).findById(carId);
        verify(carRepository, times(1)).save(car);
    }
}

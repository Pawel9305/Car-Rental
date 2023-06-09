package com.myproject.carrental.service;

import com.myproject.carrental.domain.Car;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.exception.CarNotFoundException;
import com.myproject.carrental.mapper.CarMapper;
import com.myproject.carrental.repository.CarRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;


    public void addCar(CarDto carDto) {
        carRepository.save(carMapper.mapToCar(carDto));
    }

    public void removeACar(long id) {
        carRepository.deleteById(id);
    }

    public List<CarDto> getAllCars() {
        return carMapper.mapToCarDtoList(carRepository.findAll());
    }

    public List<CarDto> getCarsByBrand(final String brand) {
        return carMapper.mapToCarDtoList(carRepository.findByBrand(brand));
    }

    public List<CarDto> getCarsByLocation(final String location) {
        return carMapper.mapToCarDtoList(carRepository.findByLocation(location));
    }

    public List<CarDto> getCarsByType(final String type) {
        return carMapper.mapToCarDtoList(carRepository.findByType(type));
    }

    public List<CarDto> getCarsPriceRangeUpTo(final BigDecimal maxValue) {
        return carMapper.mapToCarDtoList(carRepository.findAll().stream()
                .filter(car -> maxValue.compareTo(car.getPrice()) >= 0).toList());
    }

    public CarDto getById(long id) throws CarNotFoundException {
        Long iD = id;
        return carMapper.mapToCarDto(carRepository.findById(iD).orElseThrow(CarNotFoundException::new));
    }

    public void changeCarsAvailability(long id, boolean availability) throws CarNotFoundException {
        Long iD = id;
        Car car = carRepository.findById(iD).orElseThrow(CarNotFoundException::new);
        car.setAvailable(availability);
        carRepository.save(car);
    }

    public List<CarDto> carsAvailableInAGivenPeriod(LocalDate from, LocalDate to, String location) {
        List<CarDto> resultList = carMapper.mapToCarDtoList(carRepository.findAll().stream()
                .filter(car -> car.getRentals().stream()
                        .noneMatch(rental -> rental.getFrom().isBefore(to) && rental.getTo().isAfter(from)))
                .toList());

        if (location != null) {
            return resultList.stream()
                    .filter(carDto -> carDto.getLocation().equalsIgnoreCase(location))
                    .toList();
        }
        return resultList;
    }
}

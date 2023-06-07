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
        return carMapper.mapToCarDtoList(carRepository.findAll().stream().filter(car -> car.getBrand().toLowerCase().contains(brand.toLowerCase()))
                .toList());
    }

    public List<CarDto> getCarsByLocation(final String location) {
        return carMapper.mapToCarDtoList(carRepository.findAll().stream().filter(car -> car.getLocation().toLowerCase().contains(location.toLowerCase()))
                .toList());
    }

    public List<CarDto> getCarsByType(final String type) {
        return carMapper.mapToCarDtoList(carRepository.findAll().stream().filter(car -> car.getType().toLowerCase().contains(type.toLowerCase()))
                .toList());
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
}

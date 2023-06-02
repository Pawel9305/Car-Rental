package com.myproject.carrental.mapper;

import com.myproject.carrental.domain.Car;
import com.myproject.carrental.domain.CarDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class CarMapper {

    @Autowired
    private RentalMapper rentalMapper;

    public CarDto mapToCarDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getType(),
                car.getTankCapacity(),
                car.getPrice(),
                car.getLocation(),
                rentalMapper.mapToRentalDtoList(car.getRentals())
        );
    }

    public Car mapToCar(CarDto carDto) {
        return new Car(
                carDto.getId(),
                carDto.getBrand(),
                carDto.getModel(),
                carDto.getType(),
                carDto.getTankCapacity(),
                carDto.getPrice(),
                carDto.getLocation()
        );
    }

    public List<CarDto> mapToCarDtoList(List<Car> cars) {
        return cars.stream()
                .map(this::mapToCarDto)
                .toList();
    }
}

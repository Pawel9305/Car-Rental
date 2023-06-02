package com.myproject.carrental.repository;

import com.myproject.carrental.domain.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends CrudRepository<Car, Long> {

    @Override
    Car save(Car car);

    void deleteById(long id);

    @Override
    Optional<Car> findById(Long id);

    Car findById(long id);

    @Override
    List<Car> findAll();

    List<Car> findByBrand(final String brand);

    List<Car> findByType(final String type);

    List<Car> findByLocation(final String location);
}

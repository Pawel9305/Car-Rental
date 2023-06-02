package com.myproject.carrental.repository;

import com.myproject.carrental.domain.Rental;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {

    @Override
    List<Rental> findAll();

    List<Rental> findByCarId(long id);

    Optional<Rental> findById(long id);

    @Override
    Rental save(Rental rental);
}

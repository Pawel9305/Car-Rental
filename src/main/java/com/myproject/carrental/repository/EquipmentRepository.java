package com.myproject.carrental.repository;

import com.myproject.carrental.domain.AdditionalEquipment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EquipmentRepository extends CrudRepository<AdditionalEquipment, Long> {

    @Override
    AdditionalEquipment save(AdditionalEquipment additionalEquipment);

    @Override
    List<AdditionalEquipment> findAll();

    AdditionalEquipment findById(long id);

    Void deleteById(long id);
}

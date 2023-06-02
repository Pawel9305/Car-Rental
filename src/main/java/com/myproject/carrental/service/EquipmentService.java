package com.myproject.carrental.service;

import com.myproject.carrental.domain.AdditionalEquipmentDto;
import com.myproject.carrental.mapper.EquipmentMapper;
import com.myproject.carrental.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentMapper equipmentMapper;
    private final EquipmentRepository equipmentRepository;

    public List<AdditionalEquipmentDto> getAllElements() {
        return equipmentMapper.mapToAdditionalEquipmentDtoList(equipmentRepository.findAll());
    }

    public void addElement(AdditionalEquipmentDto element) {
        equipmentRepository.save(equipmentMapper.mapToAdditionalEquipment(element));
    }

    public void deleteElement(long id) {
        equipmentRepository.deleteById(id);
    }
}

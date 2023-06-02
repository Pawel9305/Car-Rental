package com.myproject.carrental.mapper;

import com.myproject.carrental.domain.AdditionalEquipment;
import com.myproject.carrental.domain.AdditionalEquipmentDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class EquipmentMapper {

    public AdditionalEquipment mapToAdditionalEquipment(AdditionalEquipmentDto additionalEquipmentDto) {
        return new AdditionalEquipment(additionalEquipmentDto.getId(),
                additionalEquipmentDto.getPrice(), additionalEquipmentDto.getDescription());
    }

    public AdditionalEquipmentDto mapToAdditionalEquipmentDto(AdditionalEquipment additionalEquipment) {
        return new AdditionalEquipmentDto(additionalEquipment.getId(),
                additionalEquipment.getPrice(), additionalEquipment.getDescription());
    }

    public List<AdditionalEquipmentDto> mapToAdditionalEquipmentDtoList(List<AdditionalEquipment> equipmentList) {
        return equipmentList.stream()
                .map(this::mapToAdditionalEquipmentDto)
                .toList();
    }
}

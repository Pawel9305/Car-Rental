package com.myproject.carrental.service;

import com.myproject.carrental.domain.AdditionalEquipment;
import com.myproject.carrental.domain.AdditionalEquipmentDto;
import com.myproject.carrental.mapper.EquipmentMapper;
import com.myproject.carrental.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EquipmentServiceTestSuite {

    @Autowired
    private EquipmentService equipmentService;

    @MockBean
    private EquipmentRepository equipmentRepository;

    @MockBean
    private EquipmentMapper equipmentMapper;

    @Test
    void testEquipmentDbServiceGetAllElements() {
        //Given
        List<AdditionalEquipmentDto> equipmentDtos = List.of(new AdditionalEquipmentDto(1, new BigDecimal("130"), "child_seat"));
        when(equipmentRepository.findAll()).thenReturn(List.of(new AdditionalEquipment(1, new BigDecimal("130"), "child_seat")));
        when(equipmentMapper.mapToAdditionalEquipmentDtoList(anyList())).thenReturn(equipmentDtos);

        //When
        List<AdditionalEquipmentDto> resultList = equipmentService.getAllElements();

        //Then
        verify(equipmentRepository, times(1)).findAll();
        assertEquals(1, resultList.size());
        assertEquals(1, resultList.get(0).getId());
    }

    @Test
    void testEquipmentDbServiceGetAllElementsEmptyList() {
        //Given
        when(equipmentRepository.findAll()).thenReturn(List.of());
        when(equipmentMapper.mapToAdditionalEquipmentDtoList(anyList())).thenReturn(List.of());

        //When
        List<AdditionalEquipmentDto> resultList = equipmentService.getAllElements();

        //Then
        verify(equipmentRepository, times(1)).findAll();
        assertEquals(0, resultList.size());
    }

    @Test
    void testEquipmentDbServiceAddElement() {
        //Given
        AdditionalEquipmentDto additionalEquipmentDto = new AdditionalEquipmentDto(1, new BigDecimal("130"), "child_seat");
        AdditionalEquipment additionalEquipment = new AdditionalEquipment(1, new BigDecimal("130"), "child_seat");
        when(equipmentMapper.mapToAdditionalEquipment(additionalEquipmentDto)).thenReturn(additionalEquipment);

        //When
        equipmentService.addElement(additionalEquipmentDto);

        //Then
        verify(equipmentRepository, times(1)).save(additionalEquipment);
    }

    @Test
    void testEquipmentDbServiceDeleteElement() {
        //Given
        //When
        equipmentService.deleteElement(1);

        //Then
        verify(equipmentRepository, times(1)).deleteById(1);
    }

}

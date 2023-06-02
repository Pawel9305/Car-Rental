package com.myproject.carrental.controller;

import com.google.gson.Gson;
import com.myproject.carrental.domain.AdditionalEquipmentDto;
import com.myproject.carrental.mapper.EquipmentMapper;
import com.myproject.carrental.service.EquipmentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(EquipmentController.class)
class EquipmentControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    @MockBean
    private EquipmentMapper equipmentMapper;

    @Test
    void shouldAddEquipment() throws Exception {
        //Given
        Gson gson = new Gson();
        AdditionalEquipmentDto additionalEquipmentDto = new AdditionalEquipmentDto(1, new BigDecimal("130"), "child_seat");
        String jsonEquip = gson.toJson(additionalEquipmentDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/equipment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(jsonEquip))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldFetchAllElements() throws Exception {
        //Given
        List<AdditionalEquipmentDto> resultList = List.of(new AdditionalEquipmentDto(1, new BigDecimal("130"), "child_seat"));
        when(equipmentService.getAllElements()).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/equipment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("child_seat")));
    }

    @Test
    void shouldFetchEmptyList() throws Exception {
        //Given
        List<AdditionalEquipmentDto> resultList = List.of();
        when(equipmentService.getAllElements()).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/equipment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldDeleteAnElement() throws Exception {
        //Given
        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/equipment/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
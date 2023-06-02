package com.myproject.carrental.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myproject.carrental.domain.CarDto;
import com.myproject.carrental.domain.LocalDateAdapter;
import com.myproject.carrental.mapper.CarMapper;
import com.myproject.carrental.service.CarService;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(CarController.class)
class CarControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private CarMapper carMapper;


    @Test
    void shouldAddACar() throws Exception {
        //Given
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        CarDto carDto = new CarDto(1, "test_brand", "test_model", "test_type",
                70, new BigDecimal("200"), "test_location");
        String carDtoJson = gson.toJson(carDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/cars/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(carDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldDeleteACar() throws Exception {
        //Given
        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/cars/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void shouldGetACarByBrand() throws Exception {
        //Given
        List<CarDto> resultList = List.of(new CarDto(1, "test_brand", "test_model", "test_type",
                70, new BigDecimal("200"), "test_location"));
        when(carService.getCarsByBrand(anyString())).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars/brand/test_brand"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand", Matchers.is("test_brand")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("test_model")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", Matchers.is("test_type")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tankCapacity", Matchers.is(70)));
    }

    @Test
    void shouldFetchEmptyBrandList() throws Exception {
        //Given
        List<CarDto> resultList = List.of();
        when(carService.getCarsByBrand(anyString())).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars/brand/test_brand"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchACarByType() throws Exception {
        //Given
        List<CarDto> resultList = List.of(new CarDto(1, "test_brand", "test_model", "test_type",
                70, new BigDecimal("200"), "test_location"));
        when(carService.getCarsByType(anyString())).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars/type/test_type"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand", Matchers.is("test_brand")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("test_model")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", Matchers.is("test_type")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tankCapacity", Matchers.is(70)));
    }

    @Test
    void shouldFetchEmptyTypeList() throws Exception {
        //Given
        List<CarDto> resultList = List.of();
        when(carService.getCarsByType(anyString())).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars/brand/test_type"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchACarByLocation() throws Exception {
        //Given
        List<CarDto> result = List.of(new CarDto(1, "test_brand", "test_model", "test_type",
                70, new BigDecimal("200"), "test_location"));
        when(carService.getCarsByLocation(anyString())).thenReturn(result);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars/location/test_location"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand", Matchers.is("test_brand")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("test_model")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", Matchers.is("test_type")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tankCapacity", Matchers.is(70)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].location", Matchers.is("test_location")));
    }

    @Test
    void shouldFetchEmptyLocationList() throws Exception {
        //Given
        List<CarDto> resultList = List.of();
        when(carService.getCarsByLocation(anyString())).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars/location/test_location"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchEmptyCarList() throws Exception {
        //Given
        when(carService.getAllCars()).thenReturn(List.of());

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }


    @Test
    void shouldReturnCarList() throws Exception {
        //Given
        when(carService.getAllCars()).thenReturn(List.of(new CarDto(1, "test_brand", "test_model", "test_type",
                70, new BigDecimal("200"), "test_location", List.of())));

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand", Matchers.is("test_brand")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Matchers.is("test_model")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", Matchers.is("test_type")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tankCapacity", Matchers.is(70)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].location", Matchers.is("test_location")));
    }

    @Test
    void shouldReturnCarsByPriceRange() throws Exception {
        //Given
        List<CarDto> resultList = List.of(new CarDto(1, "test_brand", "test_model", "test_type",
                70, new BigDecimal("200"), "test_location"));
        when(carService.getCarsPriceRangeUpTo(new BigDecimal("200"))).thenReturn(resultList);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/cars/price/200"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand", Matchers.is("test_brand")));
    }
}

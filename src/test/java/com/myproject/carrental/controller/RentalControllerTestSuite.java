package com.myproject.carrental.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myproject.carrental.domain.*;
import com.myproject.carrental.service.RentalService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(RentalController.class)
class RentalControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RentalService rentalService;

    private final long USER_ID = 1;
    private final long CAR_ID = 1;

    @Test
    void shouldRentACar() throws Exception {
        //Given
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.of(2023, 6, 4), "Warsaw", List.of());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonRequest = gson.toJson(request);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/rental")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldCalculateBasicCost() throws Exception {
        //Given
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.of(2023, 6, 4), "Warsaw", List.of());
        when(rentalService.calculateBasicCost(request.getCarId(), request.getFrom(), request.getTo())).thenReturn(new BigDecimal("200"));

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/rental/cost")
                        .param("carId", String.valueOf(CAR_ID))
                        .param("from", LocalDate.now().toString())
                        .param("to", LocalDate.of(2023, 6, 4).toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("200"));
    }

    @Test
    void shouldReturnACar() throws Exception {
        //Given
        when(rentalService.returnACar(anyLong())).thenReturn(true);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/rental/return/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void shouldCheckOverlapping() throws Exception {
        //Given
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.of(2023, 6, 4), "Warsaw", List.of());
        when(rentalService.isOverlap(request)).thenReturn(false);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonRequest = gson.toJson(request);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/rental/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    void shouldThrowOverlappingException() throws Exception {
        //Given
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.of(2023, 6, 4), "Warsaw", List.of());
        when(rentalService.isOverlap(request)).thenReturn(true);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonRequest = gson.toJson(request);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/rental/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldCheckAdditionalCosts() throws Exception {
        //Given
        RentalRequest request = new RentalRequest(USER_ID, CAR_ID, LocalDate.now(), LocalDate.of(2023, 6, 4), "Warsaw", List.of(2L, 3L));
        when(rentalService.additionalCosts(any())).thenReturn(new BigDecimal("130"));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String jsonRequest = gson.toJson(request);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/rental/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("130"));
    }

    @Test
    void shouldCheckRentalsForCar() throws Exception {
        //Given
        long carId = 1L;
        List<RentalDto> rentalDtos = List.of(new RentalDto(1L, new User(), new Car(), LocalDate.now(), LocalDate.of(2023, 6, 19), "Warsaw",
                new BigDecimal("200")));
        when(rentalService.getRentalsForCar(carId)).thenReturn(rentalDtos);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/rental/car/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].from", Matchers.equalTo(LocalDate.now().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].to", Matchers.equalTo(LocalDate.of(2023, 6, 19).toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].location", Matchers.is("Warsaw")));
    }

    @Test
    void shouldCheckRentalsForUser() throws Exception {
        //Given
        long userId = 1L;
        List<RentalDto> rentalDtos = List.of(new RentalDto(1L, new User(), new Car(), LocalDate.now(), LocalDate.of(2023, 6, 19), "Warsaw",
                new BigDecimal("200")));
        when(rentalService.getRentalsForUser(userId)).thenReturn(rentalDtos);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/rental/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].from", Matchers.equalTo(LocalDate.now().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].to", Matchers.equalTo(LocalDate.of(2023, 6, 19).toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].location", Matchers.is("Warsaw")));
    }
}

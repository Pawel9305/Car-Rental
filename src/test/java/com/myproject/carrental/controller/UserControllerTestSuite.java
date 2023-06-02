package com.myproject.carrental.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myproject.carrental.domain.LocalDateAdapter;
import com.myproject.carrental.domain.UserDto;
import com.myproject.carrental.exception.UserNotFoundException;
import com.myproject.carrental.service.UserService;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(UserController.class)
class UserControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {
        //Given
        UserDto userDto = new UserDto(1L, "test_name", "test_surname", "12345678", "test@email", List.of());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String userJson = gson.toJson(userDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        //Given
        UserDto userDto = new UserDto(1L, "test_name", "test_surname", "12345678", "test@email", List.of());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String userJson = gson.toJson(userDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        //Given
        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/users/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnUserById() throws Exception {
        //Given
        UserDto userDto = new UserDto(1L, "test_name", "test_surname", "12345678", "test@email", List.of());
        when(userService.getById(anyLong())).thenReturn(userDto);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String userJson = gson.toJson(userDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test_name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", Matchers.is("test_surname")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idNumber", Matchers.is("12345678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("test@email")));
    }

    @Test
    void shouldThrowUserNotFoundException() throws Exception {
        //Given
        when(userService.getById(anyLong())).thenThrow(new UserNotFoundException(""));
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String userJson = gson.toJson(null);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        //Given
        List<UserDto> userDtos = List.of(new UserDto(1L, "test_name", "test_surname", "12345678", "test@email", List.of()));
        when(userService.findAllUsers()).thenReturn(userDtos);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("test_name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", Matchers.is("test_surname")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idNumber", Matchers.is("12345678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("test@email")));
    }
}

package com.myproject.carrental.controller;

import com.myproject.carrental.domain.weatherforecast.Daily;
import com.myproject.carrental.service.WeatherService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(WeatherController.class)
class WeatherControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void shouldGetWeather() throws Exception {
        //Given
        List<Daily> weatherForecast = List.of( new Daily("test_time", 3, 24, 22));
        when(weatherService.getWeatherForecast(anyString())).thenReturn(weatherForecast);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/weather/Lublin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].time", Matchers.is("test_time")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].weatherCode", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].temperature_2m_max", Matchers.is(24.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].temperature_2m_min", Matchers.is(22.0)));
    }
}

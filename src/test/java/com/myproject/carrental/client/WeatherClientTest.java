package com.myproject.carrental.client;

import com.myproject.carrental.config.WeatherConfig;
import com.myproject.carrental.domain.weatherforecast.DailyTables;
import com.myproject.carrental.domain.weatherforecast.WeatherForecastDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherClientTest {

    @InjectMocks
    private WeatherClient weatherClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherConfig weatherConfig;

    @Test
    void testWeatherClient_Successful() {
        //Given
        String[] time = {"2023-05-27"};
        int[] weatherCode = {23};
        double[] temp_max = {23.5};
        double[] temp_min = {15.7};
        double latitude = 10.2567;
        double longitude = 15.2567;
        String testUrl = "http://test.url";
        DailyTables dailyTables = new DailyTables(time, weatherCode, temp_max, temp_min);
        WeatherForecastDto weatherForecastDto = new WeatherForecastDto();
        weatherForecastDto.setDaily(dailyTables);
        URI url = UriComponentsBuilder
                .fromHttpUrl(testUrl)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .build()
                .encode()
                .toUri();
        when(restTemplate.getForObject(url, WeatherForecastDto.class)).thenReturn(weatherForecastDto);
        when(weatherConfig.getWeatherUrl()).thenReturn(testUrl);

        //When
        WeatherForecastDto result = weatherClient.getWeatherForProvidedCoordinates(latitude, longitude);

        //Then
        assertNotNull(result);
    }

    @Test
    void testWeatherClient_Unsuccessful() {
        //Given
        String[] time = {"2023-05-27"};
        int[] weatherCode = {23};
        double[] temp_max = {23.5};
        double[] temp_min = {15.7};
        double latitude = 10.2567;
        double longitude = 15.2567;
        String testUrl = "http://test.url";
        DailyTables dailyTables = new DailyTables(time, weatherCode, temp_max, temp_min);
        WeatherForecastDto weatherForecastDto = new WeatherForecastDto();
        weatherForecastDto.setDaily(dailyTables);
        URI url = UriComponentsBuilder
                .fromHttpUrl(testUrl)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .build()
                .encode()
                .toUri();
        when(restTemplate.getForObject(url, WeatherForecastDto.class)).thenReturn(null);
        when(weatherConfig.getWeatherUrl()).thenReturn(testUrl);

        //When&Then
        assertThrows(NullPointerException.class, () -> weatherClient.getWeatherForProvidedCoordinates(latitude, longitude));
    }
}
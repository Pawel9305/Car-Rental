package com.myproject.carrental.client;

import com.myproject.carrental.config.WeatherConfig;
import com.myproject.carrental.domain.weatherforecast.WeatherForecastDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WeatherClient {

    private final WeatherConfig weatherConfig;
    private final RestTemplate restTemplate;

    public WeatherForecastDto getWeatherForProvidedCoordinates(double latitude, double longitude) {
        URI url = UriComponentsBuilder
                .fromHttpUrl(weatherConfig.getWeatherUrl())
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .build()
                .encode()
                .toUri();

        WeatherForecastDto response = restTemplate.getForObject(url, WeatherForecastDto.class);

        return Optional.of(response).orElse(new WeatherForecastDto());
    }
}

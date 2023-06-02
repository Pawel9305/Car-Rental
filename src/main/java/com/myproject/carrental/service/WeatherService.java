package com.myproject.carrental.service;

import com.myproject.carrental.client.WeatherClient;
import com.myproject.carrental.domain.weatherforecast.Coordinates;
import com.myproject.carrental.domain.weatherforecast.Daily;
import com.myproject.carrental.mapper.WeatherMapper;
import com.myproject.carrental.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;

    public List<Daily> getWeatherForecast(String city) {
        Coordinates coordinates = CityRepository.availableLocations().get(city);
        return WeatherMapper.mapToDaily(weatherClient.getWeatherForProvidedCoordinates(coordinates.getLatitude(),
                coordinates.getLongitude()).getDaily());
    }
}

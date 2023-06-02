package com.myproject.carrental.controller;

import com.myproject.carrental.domain.weatherforecast.Daily;
import com.myproject.carrental.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("{city}")
    public List<Daily> getWeatherFor(@PathVariable("city") final String city) {
        return weatherService.getWeatherForecast(city);
    }
}

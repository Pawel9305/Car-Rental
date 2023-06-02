package com.myproject.carrental.domain.weatherforecast;

public record Daily(String time, int weatherCode, double temperature_2m_max, double temperature_2m_min) {
}

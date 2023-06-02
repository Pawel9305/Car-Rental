package com.myproject.carrental.domain.weatherforecast;

public record DailyTables(String[] time, int[] weathercode, double[] temperature_2m_max, double[] temperature_2m_min) {
}

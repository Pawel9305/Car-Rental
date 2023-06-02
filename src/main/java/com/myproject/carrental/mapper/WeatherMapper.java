package com.myproject.carrental.mapper;

import com.myproject.carrental.domain.weatherforecast.Daily;
import com.myproject.carrental.domain.weatherforecast.DailyTables;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class WeatherMapper {

    public static List<Daily> mapToDaily(final DailyTables dailyTables) {
        List<Daily> dailies = new ArrayList<>();
        for (int i = 0; i < dailyTables.temperature_2m_max().length; i++) {
            Daily daily = new Daily(dailyTables.time()[i], dailyTables.weathercode()[i], dailyTables.temperature_2m_max()[i],
                    dailyTables.temperature_2m_min()[i]);
            dailies.add(daily);
        }
        return dailies;
    }
}

package com.myproject.carrental.repository;

import com.myproject.carrental.domain.weatherforecast.Coordinates;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class CityRepository {

    public static Map<String, Coordinates> availableLocations() {
        Map<String, Coordinates> locations = new HashMap<>();
        locations.put("lublin", new Coordinates(51.257947, 22.571037));
        locations.put("warsaw", new Coordinates(52.243755, 21.039209));
        locations.put("poznan", new Coordinates(52.392034, 16.916073));
        locations.put("gdansk", new Coordinates(54.345250, 18.638779));

        return locations;
    }
}

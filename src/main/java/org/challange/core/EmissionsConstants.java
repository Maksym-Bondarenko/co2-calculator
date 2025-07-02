package org.challange.core;

import java.util.Map;

/**
 * File for core calculation of emission.
 * Contains mapping of available car types to their emissions (in grams of CO2 per km)
 */
public final class EmissionsConstants {

    // constructor: no need to create instances
    // we only need a list (map) of hard-coded emission-values
    private EmissionsConstants() {
    }

    // final cause must be immutable
    // for current task Map is sufficient
    // for improvements separate Interfaces for cars (e.g. for car-size and/or motors) could be implemented
    private static final Map<String, Integer> EmissionsByCarType = Map.ofEntries(
            // Small Cars
            Map.entry("diesel-car-small", 142),
            Map.entry("petrol-car-small", 154),
            Map.entry("plugin-hybrid-car-small", 73),
            Map.entry("electric-car-small", 50),

            // Medium Cars
            Map.entry("diesel-car-medium", 171),
            Map.entry("petrol-car-medium", 192),
            Map.entry("plugin-hybrid-car-medium", 110),
            Map.entry("electric-car-medium", 58),

            // Large Cars
            Map.entry("diesel-car-large", 209),
            Map.entry("petrol-car-large", 282),
            Map.entry("plugin-hybrid-car-large", 126),
            Map.entry("electric-car-large", 73),

            // Others
            Map.entry("bus-default", 27),
            Map.entry("train-default", 6)
    );

    /**
     * Retrieving co2-emission in grams per km for a given car type.
     * If car type is not in the list '-1' value is returned.
     *
     * @param carType must be valid value from the emission list
     * @return emission in grams if car type is known
     */
    public static int gPerKm(String carType) {
        return EmissionsByCarType.getOrDefault(carType, -1);
    }
}
package org.challange.core;

import org.challange.core.EmissionsConstants;

public class CO2Calculator {

    /**
     *
     * @param distanceKm positive amount of kilometers
     * @param carType available type of car from {@link EmissionsConstants}
     * @return kilograms of CO2-emission calculated based on given distance and vehicle type
     * @throws IllegalArgumentException for false inputs
     */
    public static double calculate(double distanceKm, String carType) {
        // edge-cases checks
        if (distanceKm < 0.0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }

        int gPerKm = EmissionsConstants.gPerKm(carType);
        if (gPerKm < 0.0) {
            throw new IllegalArgumentException("Unknown car type: " + carType);
        }

        double kg = (gPerKm * distanceKm) / 1000.0;
        return Math.round(kg * 10) / 10.0;
    }

}
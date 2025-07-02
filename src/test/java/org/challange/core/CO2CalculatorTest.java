package org.challange.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CO2CalculatorTest {
    @Test
    void unknownCarType_throws() {
        assertThrows(IllegalArgumentException.class,
        () -> CO2Calculator.calculate(50, "non-available-car"));
    }

    @Test
    void negativeDistance_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> CO2Calculator.calculate(-5, "diesel-car-medium"));
    }

    @Test
    void dieselMedium100km_returns17_1kg() {
        double kg = CO2Calculator.calculate(100, "diesel-car-medium");
        assertEquals(17.1, kg, 0.1);
    }

    @Test
    void trainDefault42km_returns0_3kg() {
        double kg = CO2Calculator.calculate(42, "train-default");
        assertEquals(0.3, kg, 0.1);
    }
}
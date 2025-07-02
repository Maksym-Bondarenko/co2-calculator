package org.challange;

import org.challange.api.DistanceService;
import org.challange.api.GeocodingService;
import org.challange.api.dto.Coordinates;
import org.challange.cli.ArgumentParser;
import org.challange.cli.CLIOptions;
import org.challange.core.CO2Calculator;

public class Main {
    public static void main(String[] args) {
        try {
            CLIOptions options = ArgumentParser.parse(args);

            // 1) get distance between 2 given locations via external API
            // 1.1) get geocoding coordinates (long-lat) of both cities via {@link GeocodingService}
            // 1.2) get distance in km between 2 found cities
            // 2) calculate CO2-emission of given car-type and extracted distance

            double distanceKm = fetchDistance(options.start(), options.destination());
            double kg = CO2Calculator.calculate(distanceKm, options.carType());

            System.out.printf("Your trip caused %.1fkg of CO2-equivalent.%n", kg);

//            // to test external API
//            GeocodingService geo = new GeocodingService();
//            Coordinates berlin  = geo.findCity("Berlin").orElseThrow();
//            Coordinates hamburg = geo.findCity("Hamburg").orElseThrow();
//            System.out.println("Coordinates: " + berlin + ", " + hamburg);
//
//            double km = new DistanceService().distanceKm(hamburg, berlin).orElseThrow();
//            System.out.println("Hamburg → Berlin ≈ " + km + " km");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Private help-function to get coordinates of given cities via geocoding external API,
     * and call matrix to calculate distance in km between given cities
     *
     * @param startCity name of city A (start)
     * @param endCity name of city B (destination)
     * @return distance in km between 2 cities
     */
    private static double fetchDistance(String startCity, String endCity) {

        GeocodingService geo = new GeocodingService();
        Coordinates from = geo.findCity(startCity)
                .orElseThrow(() ->
                        new IllegalArgumentException("Could not geocode start city: " + startCity));
        Coordinates to   = geo.findCity(endCity)
                .orElseThrow(() ->
                        new IllegalArgumentException("Could not geocode destination city: " + endCity));

        return new DistanceService().distanceKm(from, to)
                .orElseThrow(() ->
                        new IllegalStateException("Distance API failed for those coordinates"));
    }
}
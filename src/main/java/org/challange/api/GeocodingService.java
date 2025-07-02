package org.challange.api;

import org.challange.api.dto.Coordinates;

import org.json.*;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

/**
 * Service to get via external API (openrouteservice.org) coordinates of given cities
 * Here is included JSON-parsing, http request/response adjustments, checks for input/output, etc.
 */
public final class GeocodingService implements CommonAPIService {
    /**
     * Constructor with check of ORS_TOKEN environment variable
     */
    public GeocodingService() {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Missing ORS_TOKEN environment variable!");
        }
    }

    /**
     * Finds a city (if exists and if returned via API -> so 'Optional' type)
     *
     * @param city name of city to find via API
     * @return {@link Coordinates} of city if it was found: longitude and latitude
     */
    public Optional<Coordinates> findCity(String city) {
        String url = API_ENDPOINT_GEOCODE +
                "api_key=" + token +
                "&text=" +
                URLEncoder.encode(city, StandardCharsets.UTF_8) +
                "&layers=locality";

        // builds a GET-request with headers and token as described in challenge
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // not OK
            if(response.statusCode() != 200)
                return Optional.empty();

            // for now, we can directly search for coordinates, but for larger project the JSON-mapping would be nicer
            return extractCityCoordinates(response.body());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Private help-function to get coordinates (longitude, latitude)-array from JSON-response of external API
     *
     * @param json API response
     * @return coordinates (if they are found and empty otherwise)
     */
    private static Optional<Coordinates> extractCityCoordinates(String json) {
        // For larger projects is better to build separate JSON parser, current one is lightweight to keep it simple
        try {
            JSONObject root      = new JSONObject(json);
            JSONArray  features  = root.getJSONArray("features");

            // probably error with json-response
            if (features.isEmpty())
                return Optional.empty();

            // getting first a pair of coordinates
            JSONArray coords = features.getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONArray("coordinates");

            return Optional.of(new Coordinates(coords.getDouble(0), coords.getDouble(1)));
        } catch (NumberFormatException ex) {
            // for debugging
            System.out.println("Error: " + ex.getMessage());
            System.out.println("JSON: " + json);
            return Optional.empty();
        }
    }
}
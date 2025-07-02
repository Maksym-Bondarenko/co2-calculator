package org.challange.api;

import org.challange.api.dto.Coordinates;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Service to get via external API (openrouteservice.org) driving distance between 2 pairs of {@link Coordinates}: longitude and latitude
 * Here is included JSON-parsing, http request/response adjustments, checks for input/output, etc.
 */
public final class DistanceService implements CommonAPIService {
    // for current project is okay to have fewer files and be lightweight
    // but for bigger - a common ancestor for HTTP services (with token, endpoint, etc.) would be nicer
    // -> distance, geocoding and other services will follow it

    /**
     * Constructor with check of ORS_TOKEN environment variable
     */
    public DistanceService() {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Missing ORS_TOKEN environment variable!");
        }
    }

    /**
     * Calculates distance (car driving mode) in km via external API for 2 given pair of {@link Coordinates}
     *
     * @param from long-lat coordinates-pair of start
     * @param to long-lat coordinates-pair of destination
     * @return double-value of kilometers distance (if input and API-call are correct, empty otherwise)
     */
    public Optional<Double> distanceKm(Coordinates from, Coordinates to) {
        String url = API_ENDPOINT_DISTANCE;

        // request-body as described in challange-info
        String body = """
                {
                    "locations": [
                        [%f, %f],
                        [%f, %f]
                    ],
                    "metrics": ["distance"],
                    "units": "km"
                }
                """.formatted(from.latitude(), from.longitude(), to.latitude(), to.longitude());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // not OK
            if (response.statusCode() != 200)
                return Optional.empty();

            // parse response-json to retrieve distance in km
            return extractFirstDistance(response.body());

        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    /**
     * Helper-function to extract fron response-json distance in km
     * @param json response
     * @return distance in km as double if found (empty otherwise)
     */
    private static Optional<Double> extractFirstDistance(String json) {
        try {
            JSONObject root     = new JSONObject(json);
            JSONArray distances = root.getJSONArray("distances");

            if (distances.isEmpty())
                return Optional.empty();

            double km = distances.getJSONArray(0).getDouble(1);
            return Optional.of(km);

        } catch (Exception ex) {
            // for debugging
            System.out.println("Error: " + ex.getMessage());
            System.out.println("JSON: " + json);
            return Optional.empty();
        }
    }
}
package org.challange.api;

import java.net.http.HttpClient;
import java.time.Duration;

public interface CommonAPIService {
    /**
     * geodocing API Endpoint to get city coordinates and distance between cities
     */
    String API_ENDPOINT_GEOCODE = "https://api.openrouteservice.org/geocode/search?";

    /**
     * geodocing API Endpoint to get city coordinates and distance between cities
     */
    String API_ENDPOINT_DISTANCE = "https://api.openrouteservice.org/v2/matrix/driving-car";

    /**
     * http client with default timeout duration of 10 seconds for API calls
     */
    HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    /**
     * personal token (should not be shared, saved either in .env or in project configurations)
     */
    String token = System.getenv("ORS_TOKEN");
}

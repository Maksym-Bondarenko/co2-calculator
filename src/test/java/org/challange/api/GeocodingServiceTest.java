package org.challange.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.challange.api.dto.Coordinates;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class GeocodingServiceTest {
    // mocking API server for responses
    private static WireMockServer wm;

    // start mock server
    @BeforeAll
    static void startStub() {
        wm = new WireMockServer(0);   // random free port
        wm.start();
        System.setProperty("API_ENDPOINT_DISTANCE", "http://localhost:" + wm.port());
        // IMPORTANT! Do not forget to put <ORS_TOKEN> into environment variables!
    }

    // stop mock server
    @AfterAll
    static void stopStub() { wm.stop(); }

    @Test
    void fictionalCity_returnsEmpty() {
        wm.stubFor(get(anyUrl()).willReturn(aResponse().withStatus(404)));
        assertTrue(new GeocodingService().findCity("Arrakis").isEmpty());
    }

    @Test
    void happyPath_returnsCoordinates() {
        Coordinates c = new GeocodingService().findCity("Munich").orElseThrow();
        assertEquals(48.152126, c.longitude());
        assertEquals(11.544467, c.latitude());
    }
}
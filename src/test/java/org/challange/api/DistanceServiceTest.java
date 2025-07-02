package org.challange.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.challange.api.dto.Coordinates;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistanceServiceTest {
    private static WireMockServer wm;

    @BeforeAll
    static void startStub() {
        wm = new WireMockServer(0);
        wm.start();
        System.setProperty("API_ENDPOINT_GEOCODE", "http://localhost:" + wm.port());
        // IMPORTANT! Do not forget to put <ORS_TOKEN> into environment variables!
    }

    @AfterAll
    static void stopStub() { wm.stop(); }

    @Test
    @Disabled
    void sameCity_returnsZero() {
        wm.stubFor(post(urlPathMatching("/v2/matrix/.*"))
                .willReturn(okJson("""
                       {"distances":[[0,0],[0,0]]}
                       """)));

        Coordinates c = new Coordinates(10, 20);
        // same coordinates of 'city'
        double km = new DistanceService().distanceKm(c, c).orElseThrow();
        assertEquals(0.0, km);
    }

    @Test
    @Disabled
    void farCities_bigNumber() {
        wm.stubFor(post(anyUrl()).willReturn(okJson("""
            {"distances":[[0,12345.6],[12345.6,0]]}
            """)));

        double km = new DistanceService().distanceKm(
                new Coordinates(0,0), new Coordinates(1,1)).orElseThrow();
        assertTrue(km > 10000);
    }
}
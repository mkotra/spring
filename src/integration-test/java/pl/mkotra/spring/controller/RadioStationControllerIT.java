package pl.mkotra.spring.controller;

import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.model.RadioStation;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RadioStationControllerIT extends BaseIT {

    @Autowired
    RadioStationService radioStationService;

    @Test
    void pullRadioStationsTest() {
        // You can use the DSL directly from the extension field
        wireMockExtension.stubFor(get("/json/stations?limit=10").willReturn(
                aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(toJson(Lists.emptyList()))
        ));

        //when
        List<RadioStation> result = webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/radio-stations/pull")
                        .queryParam("limit", 10)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(RadioStation.class)
                .returnResult()
                .getResponseBody();


        //then
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}

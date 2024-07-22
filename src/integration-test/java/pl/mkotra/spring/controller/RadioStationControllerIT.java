package pl.mkotra.spring.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.integration.RadioBrowserStation;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RadioStationControllerIT extends BaseIT {

    @Autowired
    RadioStationService radioStationService;

    @Test
    void pullRadioStationsTest() throws Exception {
        String requestBody = """ 
                        [
                            {
                                "stationuuid" : "123e4567-e89b-12d3-a456-426655440000",
                                "name" : "Radio 1",
                                "country" : "Poland"
                            }
                        ]
                """;

        wireMockExtension.stubFor(WireMock.get("/json/stations?limit=10").willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(requestBody)
        ));

        mockMvc.perform(post("/radio-stations/pull?limit=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(notNullValue()))
                .andExpect(jsonPath("$[0].name").value(is("Radio 1")))
                .andExpect(jsonPath("$[0].country").value(is("Poland")))
                .andExpect(jsonPath("$[0].timestamp").value(notNullValue()));

        mockMvc.perform(get("/radio-stations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(notNullValue()))
                .andExpect(jsonPath("$[0].name").value(is("Radio 1")))
                .andExpect(jsonPath("$[0].country").value(is("Poland")))
                .andExpect(jsonPath("$[0].timestamp").value(notNullValue()));
    }
}

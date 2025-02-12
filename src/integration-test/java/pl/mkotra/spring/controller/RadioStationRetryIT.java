package pl.mkotra.spring.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.mkotra.spring.BaseIT;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RadioStationRetryIT extends BaseIT {

    @Test
    void retriesRequest() throws Exception {
        wireMockExtension.stubFor(WireMock.get("/json/stations?limit=10")
                .inScenario("Retry Scenario")
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("Second Failure")
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {"error":"Server Error"}"""
                        )
                )
        );

        wireMockExtension.stubFor(WireMock.get("/json/stations?limit=10")
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Second Failure")
                .willSetStateTo("Success")
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {"error":"Server Error"}"""
                        )
                )
        );

        wireMockExtension.stubFor(WireMock.get("/json/stations?limit=10")
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Success")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(ResponseStubs.STUB_RESPONSE_BODY)
                )
        );

        mockMvc.perform(post("/radio-stations/pull").param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)));
    }

    @Test
    void retriesRequestAndFailsAfterMaxAttempts() throws Exception {
        wireMockExtension.stubFor(WireMock.get("/json/stations?limit=10")
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "error" : "Server Error"
                                }"""
                        )
                )
        );

        mockMvc.perform(post("/radio-stations/pull").param("limit", "10"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}

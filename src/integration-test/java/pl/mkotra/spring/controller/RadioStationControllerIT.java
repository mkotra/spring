package pl.mkotra.spring.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.mkotra.spring.BaseIT;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;

import java.time.Instant;
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
    RadioStationRepository radioStationRepository;

    @BeforeEach
    void setUp() {
        radioStationRepository.deleteAll();
    }

    @Test
    void pullStations() throws Exception {
        wireMockExtension.stubFor(WireMock.get("/json/stations?limit=10").willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(ResponseStubs.STUB_RESPONSE_BODY)
        ));

        mockMvc.perform(post("/radio-stations/pull").param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(notNullValue()))
                .andExpect(jsonPath("$[0].uuid").value(notNullValue()))
                .andExpect(jsonPath("$[0].name").value(is("Radio 1")))
                .andExpect(jsonPath("$[0].country").value(is("Poland")))
                .andExpect(jsonPath("$[0].url").value(is("https://www.example.com/test.pls")))
                .andExpect(jsonPath("$[0].tags").isArray())
                .andExpect(jsonPath("$[0].tags").value(hasItems("jazz", "pop", "rock", "indie")))
                .andExpect(jsonPath("$[0].timestamp").value(notNullValue()));
    }

    @Test
    void pullWithInvalidLimit() throws Exception {
        mockMvc.perform(post("/radio-stations/pull").param("limit", "1001"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll() throws Exception {
        radioStationRepository.save(new RadioStationDB(null, "123e4567-e89b-12d3-a456-426655440000", "Radio 1", "Poland",
                "https://www.example.com/test.pls", List.of("jazz", "pop", "rock", "indie"), Instant.now()));

        mockMvc.perform(get("/radio-stations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(notNullValue()))
                .andExpect(jsonPath("$[0].uuid").value(is("123e4567-e89b-12d3-a456-426655440000")))
                .andExpect(jsonPath("$[0].name").value(is("Radio 1")))
                .andExpect(jsonPath("$[0].country").value(is("Poland")))
                .andExpect(jsonPath("$[0].url").value(is("https://www.example.com/test.pls")))
                .andExpect(jsonPath("$[0].tags").isArray())
                .andExpect(jsonPath("$[0].tags").value(hasItems("jazz", "pop", "rock", "indie")))
                .andExpect(jsonPath("$[0].timestamp").value(notNullValue()));
    }

    @Test
    void getAllWithEmptyResponse() throws Exception {
        mockMvc.perform(get("/radio-stations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(0)));
    }

    @Test
    void search() throws Exception {
        radioStationRepository.save(new RadioStationDB(null, "123e4567-e89b-12d3-a456-426655440000", "Radio 1", "Poland",
                "https://www.example.com/test.pls", List.of("jazz", "pop", "rock", "indie"), Instant.now()));

        mockMvc.perform(get("/radio-stations/search").param("tags", "jazz", "pop"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(notNullValue()))
                .andExpect(jsonPath("$[0].uuid").value(is("123e4567-e89b-12d3-a456-426655440000")))
                .andExpect(jsonPath("$[0].name").value(is("Radio 1")))
                .andExpect(jsonPath("$[0].country").value(is("Poland")))
                .andExpect(jsonPath("$[0].url").value(is("https://www.example.com/test.pls")))
                .andExpect(jsonPath("$[0].tags").isArray())
                .andExpect(jsonPath("$[0].tags").value(hasItems("jazz", "pop", "rock", "indie")))
                .andExpect(jsonPath("$[0].timestamp").value(notNullValue()));
    }

    @Test
    void searchWithoutTags() throws Exception {
        mockMvc.perform(get("/radio-stations/search"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById() throws Exception {
        RadioStationDB radioStationDB = radioStationRepository.save(new RadioStationDB(null, "123e4567-e89b-12d3-a456-426655440000", "Radio 1", "Poland",
                "https://www.example.com/test.pls", List.of("jazz", "pop", "rock", "indie"), Instant.now()));

        mockMvc.perform(get("/radio-stations/{id}", radioStationDB.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.uuid").value(is("123e4567-e89b-12d3-a456-426655440000")))
                .andExpect(jsonPath("$.name").value(is("Radio 1")))
                .andExpect(jsonPath("$.country").value(is("Poland")))
                .andExpect(jsonPath("$.url").value(is("https://www.example.com/test.pls")))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags").value(hasItems("jazz", "pop", "rock", "indie")))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()));
    }

    @Test
    void getByIdWhenDoesNotExist() throws Exception {
        mockMvc.perform(get("/radio-stations/{id}", "not_existing_id"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

package pl.mkotra.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.model.RadioStation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemControllerIT extends BaseIT {

    @Autowired
    RadioStationService itemService;

    @Test
    void simpleTest() {
        //given
        itemService.save(new RadioStation(null, "Radio 1", "Poland"))
                .block();

        //when
        List<RadioStation> result = webTestClient.get()
                .uri("/radio-station")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(RadioStation.class)
                .returnResult()
                .getResponseBody();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).id());
        assertNotNull(result.get(0).name());
    }
}

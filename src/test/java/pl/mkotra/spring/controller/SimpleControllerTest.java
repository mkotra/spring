package pl.mkotra.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mkotra.spring.domain.Item;
import pl.mkotra.spring.domain.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleControllerTest extends BaseControllerTest {

    //spring application beans available in tests context
    @Autowired
    ItemService itemService;

    @Test
    void simpleTest() {
        List<Item> result = webTestClient.get()
                .uri("/items/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Item.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getId());
    }
}

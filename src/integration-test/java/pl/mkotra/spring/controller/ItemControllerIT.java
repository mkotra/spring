package pl.mkotra.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mkotra.spring.core.ItemService;
import pl.mkotra.spring.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemControllerIT extends BaseIT {

    @Autowired
    ItemService itemService;

    @Test
    void simpleTest() {
        //given
        itemService.save(new Item(null, "Some item"))
                .block();

        //when
        List<Item> result = webTestClient.get()
                .uri("/items")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Item.class)
                .returnResult()
                .getResponseBody();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).id());
        assertNotNull(result.get(0).name());
    }
}

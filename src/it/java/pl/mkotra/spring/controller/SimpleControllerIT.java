package pl.mkotra.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mkotra.spring.storage.Item;
import pl.mkotra.spring.storage.ItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleControllerIT extends BaseIT {

    @Autowired
    ItemRepository itemRepository;

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
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getId());
        assertNotNull(result.get(0).getName());
    }

    @Test
    void simpleTest2() {
        Item item = Item.of("id", "name");
        itemRepository.save(item);
        Optional<Item> result = itemRepository.findById("id");

        assertTrue(result.isPresent());
        assertEquals("id", result.get().getId());
    }
}

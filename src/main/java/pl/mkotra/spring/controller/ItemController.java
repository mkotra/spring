package pl.mkotra.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mkotra.spring.core.ItemService;
import pl.mkotra.spring.model.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PutMapping
    public Mono<Item> save() {
        Item item = new Item(null, "NAME - " + UUID.randomUUID());
        return itemService.save(item);
    }

    @GetMapping
    public Flux<Item> findAll() {
        return itemService.findAll();
    }
}

package pl.mkotra.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.mkotra.spring.storage.Item;
import pl.mkotra.spring.storage.ItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class SimpleController {

    private final ItemRepository itemRepository;

    @RequestMapping(value = "/mono", method = RequestMethod.GET)
    public Mono<Item> mono() {
        log.info("ACCEPTED");
        return Mono.just(Item.of("1", "test" ));
    }

    @RequestMapping(value = "/flux", method = RequestMethod.GET)
    public Flux<Item> flux() {
        String id = UUID.randomUUID().toString();

        itemRepository.save(Item.of(UUID.randomUUID().toString(), "Item " + id));
        return Flux.fromIterable(itemRepository.findAll());
    }

    @RequestMapping(value = "/range", method = RequestMethod.GET)
    public Flux<Integer> range() {
        log.info("ACCEPTED");
        return Flux.range(1, 10).delayElements(Duration.ofSeconds(1));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Integer> list() {
        log.info("ACCEPTED");
        return List.of(1, 2, 3, 4, 5, 6);
    }
}

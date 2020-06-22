package pl.mkotra.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.mkotra.spring.domain.Item;
import pl.mkotra.spring.domain.ItemService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class SimpleController {

    private final ItemService itemService;

    @RequestMapping(value = "/mono", method = RequestMethod.GET)
    public Mono<Item> mono() {
        log.info("ACCEPTED");
        return Mono.just(Item.of(123));
    }

    @RequestMapping(value = "/flux", method = RequestMethod.GET)
    public Flux<Item> flux() {
        log.info("ACCEPTED :" + itemService.item());
        return Flux.fromIterable(List.of(Item.of(1), Item.of(2), Item.of(3)));
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

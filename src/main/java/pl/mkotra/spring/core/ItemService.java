package pl.mkotra.spring.core;

import org.springframework.stereotype.Service;
import pl.mkotra.spring.model.Item;
import pl.mkotra.spring.storage.ItemDB;
import pl.mkotra.spring.storage.ItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository repository) {
        this.itemRepository = repository;
    }

    public Flux<Item> findAll() {
        return itemRepository.findAll().map(ItemMapper.INSTANCE::map);
    }

    public Mono<Item> save(Item item) {
        Mono<ItemDB> saved = itemRepository.save(ItemMapper.INSTANCE.map(item));
        return saved.map(ItemMapper.INSTANCE::map);
    }
}

package pl.mkotra.spring;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.core.ItemService;
import pl.mkotra.spring.model.Item;
import pl.mkotra.spring.storage.ItemDB;
import pl.mkotra.spring.storage.ItemRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    ItemRepository itemRepository = mock(ItemRepository.class);
    ItemService itemService = new ItemService(itemRepository);

    @Test
    public void simpleUnitTest() {
        //given
        when(itemRepository.findAll()).thenReturn(Flux.just(new ItemDB("ID", "NAME")));

        //when
        Flux<Item> itemFlux = itemService.findAll();

        //then
        List<Item> items = itemFlux.toStream().collect(Collectors.toList());
        assertEquals(1, items.size());
        assertEquals("ID", items.get(0).id());
    }
}

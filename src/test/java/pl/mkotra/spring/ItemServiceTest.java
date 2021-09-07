package pl.mkotra.spring;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    RadioStationRepository radioStationRepository = mock(RadioStationRepository.class);
    RadioBrowserAdapter radioBrowserAdapter = mock(RadioBrowserAdapter.class);

    RadioStationService radioStationService = new RadioStationService(radioStationRepository, radioBrowserAdapter);

    @Test
    public void simpleUnitTest() {
        //given
        when(radioStationRepository.findAll()).thenReturn(Flux.just(
                new RadioStationDB("ID", "Radio 1", "Poland", OffsetDateTime.now()))
        );

        //when
        Flux<RadioStation> itemFlux = radioStationService.findAll();

        //then
        List<RadioStation> radioStations = itemFlux.toStream().collect(Collectors.toList());
        assertEquals(1, radioStations.size());
        assertEquals("ID", radioStations.get(0).id());
    }
}

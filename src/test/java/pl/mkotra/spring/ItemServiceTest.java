package pl.mkotra.spring;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    RadioStationRepository radioStationRepository = mock(RadioStationRepository.class);
    RadioBrowserAdapter radioBrowserAdapter = mock(RadioBrowserAdapter.class);

    RadioStationService radioStationService = new RadioStationService(radioStationRepository, radioBrowserAdapter);

    @Test
    public void simpleUnitTest() {
        //given
        when(radioStationRepository.findAll()).thenReturn(List.of(
                new RadioStationDB("ID", "Radio 1", "Poland", OffsetDateTime.now()))
        );

        //when
        List<RadioStation> result = radioStationService.findAll();

        //then
        assertEquals(1, result.size());
        assertEquals("ID", result.getFirst().id());
    }
}

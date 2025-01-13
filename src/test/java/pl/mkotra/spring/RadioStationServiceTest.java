package pl.mkotra.spring;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RadioStationServiceTest {

    RadioStationRepository radioStationRepository = mock(RadioStationRepository.class);
    RadioBrowserAdapter radioBrowserAdapter = mock(RadioBrowserAdapter.class);

    RadioStationService radioStationService = new RadioStationService(radioStationRepository, radioBrowserAdapter);

    @Test
    public void simpleUnitTest() {
        //given
        when(radioStationRepository.findAll()).thenReturn(List.of(
                new RadioStationDB("ID", "Radio 1", "Poland", OffsetDateTime.now()),
                new RadioStationDB("ID", "Radio 2", "Poland", OffsetDateTime.now()))
        );

        //when
        List<RadioStation> result = radioStationService.findAll();

        //then
        assertThat(result)
                .hasSize(2)
                .allSatisfy(radioStation -> {
                    assertThat(radioStation.name()).isNotNull();
                });
    }
}

package pl.mkotra.spring;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.integration.KafkaProducer;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RadioStationServiceTest {

    RadioStationRepository radioStationRepository = mock(RadioStationRepository.class);
    RadioBrowserAdapter radioBrowserAdapter = mock(RadioBrowserAdapter.class);
    KafkaProducer kafkaProducer = mock(KafkaProducer.class);

    RadioStationService radioStationService = new RadioStationService(radioStationRepository, radioBrowserAdapter, kafkaProducer);

    @Test
    public void simpleUnitTest() {
        //given
        when(radioStationRepository.findAll()).thenReturn(List.of(
                new RadioStationDB("id_1", "uuid_1", "Radio 1", "Poland", Instant.now()),
                new RadioStationDB("id_1", "uuid_2", "Radio 2", "Poland", Instant.now()))
        );

        //when
        List<RadioStation> result = radioStationService.findAll();

        //then
        assertThat(result)
                .hasSize(2)
                .allSatisfy(radioStation -> {
                    assertThat(radioStation.id()).isNotNull();
                    assertThat(radioStation.uuid()).isNotNull();
                    assertThat(radioStation.name()).isNotNull();
                    assertThat(radioStation.country()).isNotNull();
                    assertThat(radioStation.timestamp()).isNotNull();
                });
    }
}

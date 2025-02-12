package pl.mkotra.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.*;

public class RadioStationServiceTest {

    RadioStationRepository radioStationRepository = mock(RadioStationRepository.class);
    RadioBrowserAdapter radioBrowserAdapter = mock(RadioBrowserAdapter.class);

    RadioStationService radioStationService = new RadioStationService(radioStationRepository, radioBrowserAdapter);

    @Test
    @DisplayName("pull saves radio stations and returns valid response")
    public void pull() {
        //given
        int limit = 100;
        when(radioBrowserAdapter.getRadioStations(limit)).thenReturn(List.of(
                new RadioStation("id_1", "uuid_1", "Radio 1", "Poland", "url1", List.of("jazz,rock"), OffsetDateTime.now()),
                new RadioStation("id_2", "uuid_2", "Radio 2", "Poland", "url2", List.of("news"), OffsetDateTime.now()))
        );
        when(radioStationRepository.saveAll(anyList())).thenReturn(List.of(
                new RadioStationDB("id_1", "uuid_1", "Radio 1", "Poland", "url1", List.of("jazz,rock"), Instant.now()),
                new RadioStationDB("id_2", "uuid_2", "Radio 2", "Poland", "url2", List.of("news"), Instant.now()))
        );

        //when
        List<RadioStation> result = radioStationService.pull(limit);

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

        verify(radioBrowserAdapter).getRadioStations(limit);
        verify(radioStationRepository).deleteAll();
        verify(radioStationRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("findAll returns valid response")
    public void findAll() {
        //given
        when(radioStationRepository.findAll()).thenReturn(List.of(
                new RadioStationDB("id_1", "uuid_1", "Radio 1", "Poland", "url1", List.of("jazz,rock"), Instant.now()),
                new RadioStationDB("id_2", "uuid_2", "Radio 2", "Poland", "url2", List.of("news"), Instant.now()))
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

    @Test
    @DisplayName("find returns valid response")
    public void find() {
        //given
        String id = "id_1";
        var radioStation = new RadioStationDB("id_1", "uuid_1", "Radio 1", "Poland", "url1", List.of("jazz,rock"), Instant.now());
        when(radioStationRepository.findById(id)).thenReturn(Optional.of(radioStation));

        //when
        Optional<RadioStation> result = radioStationService.find(id);

        //then
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value.id()).isEqualTo(radioStation.id());
                    assertThat(value.uuid()).isEqualTo(radioStation.uuid());
                    assertThat(value.name()).isEqualTo(radioStation.name());
                    assertThat(value.country()).isEqualTo(radioStation.country());
                    assertThat(value.timestamp().toInstant())
                            .isCloseTo(radioStation.timestamp(), within(1, ChronoUnit.MILLIS));
                });
    }
}

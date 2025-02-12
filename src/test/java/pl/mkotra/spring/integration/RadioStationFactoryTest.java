package pl.mkotra.spring.integration;

import org.junit.jupiter.api.Test;
import pl.mkotra.spring.model.RadioStation;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RadioStationFactoryTest {

    private final RadioStationFactory radioStationFactory = new RadioStationFactory();

    @Test
    void create_withValidRadioBrowserStation_shouldCreateRadioStation() {
        //given
        RadioBrowserStation radioBrowserStation = radioBrowserStations("music, sports");
        OffsetDateTime timestamp = OffsetDateTime.now();

        //when
        RadioStation result = radioStationFactory.create(radioBrowserStation, timestamp);

        //then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNull();
        assertThat(result.uuid()).isNotNull();
        assertThat(result.name()).isEqualTo("Test Station");
        assertThat(result.country()).isEqualTo("Test Country");
        assertThat(result.url()).isEqualTo( "https://test.url");
        assertThat(result.tags()).containsExactly("music", "sports");
        assertThat(result.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void create_withNullTags_shouldCreateRadioStationWithEmptyTags() {
        //given
        RadioBrowserStation radioBrowserStation = radioBrowserStations(null);
        OffsetDateTime timestamp = OffsetDateTime.now();

        //when
        RadioStation result = radioStationFactory.create(radioBrowserStation, timestamp);

        //then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNull();
        assertThat(result.uuid()).isNotNull();
        assertThat(result.name()).isEqualTo("Test Station");
        assertThat(result.country()).isEqualTo("Test Country");
        assertThat(result.url()).isEqualTo( "https://test.url");
        assertThat(result.tags()).isEmpty();
        assertThat(result.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void create_withEmptyTags_shouldCreateRadioStationWithEmptyTags() {
        //given
        RadioBrowserStation radioBrowserStation = radioBrowserStations("");
        OffsetDateTime timestamp = OffsetDateTime.now();

        //when
        RadioStation result = radioStationFactory.create(radioBrowserStation, timestamp);

        //then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNull();
        assertThat(result.uuid()).isNotNull();
        assertThat(result.name()).isEqualTo("Test Station");
        assertThat(result.country()).isEqualTo("Test Country");
        assertThat(result.url()).isEqualTo( "https://test.url");
        assertThat(result.tags()).isEmpty();
        assertThat(result.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void create_withMultipleTags_shouldCreateRadioStationWithCorrectTags() {
        //given
        RadioBrowserStation radioBrowserStation = radioBrowserStations("music, news, sports");
        OffsetDateTime timestamp = OffsetDateTime.now();

        //when
        RadioStation result = radioStationFactory.create(radioBrowserStation, timestamp);

        //then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNull();
        assertThat(result.uuid()).isNotNull();
        assertThat(result.name()).isEqualTo("Test Station");
        assertThat(result.country()).isEqualTo("Test Country");
        assertThat(result.url()).isEqualTo( "https://test.url");
        assertThat(result.tags()).containsExactly("music", "news", "sports");
        assertThat(result.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void create_withNullTimestamp_shouldCreateRadioStationWithNullTimestamp() {
        //given
        RadioBrowserStation radioBrowserStation = radioBrowserStations("music, news, sports");

        //when
        RadioStation result = radioStationFactory.create(radioBrowserStation, null);

        //then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNull();
        assertThat(result.uuid()).isNotNull();
        assertThat(result.name()).isEqualTo("Test Station");
        assertThat(result.country()).isEqualTo("Test Country");
        assertThat(result.url()).isEqualTo( "https://test.url");
        assertThat(result.tags()).containsExactly("music", "news", "sports");
        assertThat(result.timestamp()).isNull();
    }

    private RadioBrowserStation radioBrowserStations(String tags) {
       return new RadioBrowserStation(
                "stationuuid", "Test Station", "Test Country", "https://test.url", tags);
    }
}

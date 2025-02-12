package pl.mkotra.spring.integration;

import org.springframework.stereotype.Component;
import pl.mkotra.spring.model.RadioStation;

import java.time.OffsetDateTime;
import java.util.*;

@Component
class RadioStationFactory {

    RadioStation create(RadioBrowserStation radioBrowserStation, OffsetDateTime timestamp) {

        return new RadioStation(null,
                UUID.randomUUID().toString(),
                radioBrowserStation.name(),
                radioBrowserStation.country(),
                radioBrowserStation.url(),
                tags(radioBrowserStation.tags()),
                timestamp
        );
    }

    List<String> tags(String tags) {
        return Optional.ofNullable(tags)
                .map(t -> t.split(",")).stream()
                .flatMap(Arrays::stream)
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .toList();
    }

}

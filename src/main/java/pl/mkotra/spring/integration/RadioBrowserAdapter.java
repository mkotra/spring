package pl.mkotra.spring.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.mkotra.spring.model.RadioStation;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class RadioBrowserAdapter {

    private final Supplier<OffsetDateTime> timeSupplier;
    private final RestClient restClient;

    RadioBrowserAdapter(Supplier<OffsetDateTime> timeSupplier,
                        RestClient restClient) {
        this.restClient = restClient;
        this.timeSupplier = timeSupplier;
    }

    public List<RadioStation> getRadioStations(int limit) {
        OffsetDateTime timestamp = timeSupplier.get();

        RadioBrowserStation[] radioBrowserStations = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/json/stations")
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(RadioBrowserStation[].class);

        return Optional.ofNullable(radioBrowserStations)
                .stream()
                .flatMap(Arrays::stream)
                .map(s -> createRadioStation(s.name().trim(), s.country(), timestamp))
                .toList();
    }

    private RadioStation createRadioStation(String name, String country, OffsetDateTime timestamp) {
        return new RadioStation(null, UUID.randomUUID().toString(), name, country, timestamp);
    }
}

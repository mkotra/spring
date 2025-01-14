package pl.mkotra.spring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    private final RestClient restClient;
    private final Supplier<OffsetDateTime> timeSupplier;

    public RadioBrowserAdapter(@Value("${integration.radio-browser-api-url}") String radioBrowserApiUrl,
                               Supplier<OffsetDateTime> timeSupplier) {

        restClient = RestClient.builder()
                .baseUrl(radioBrowserApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.timeSupplier = timeSupplier;
    }

    public List<RadioStation> getRadioStations(int limit) {
        OffsetDateTime timestamp = timeSupplier.get();
        RadioBrowserStation[] radioBrowserStations = restClient.get()
                .uri("/json/stations?limit=" + limit)
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

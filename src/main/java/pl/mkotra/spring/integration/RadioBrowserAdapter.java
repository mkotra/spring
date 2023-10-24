package pl.mkotra.spring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.mkotra.spring.model.RadioStation;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class RadioBrowserAdapter {

    private final RestTemplate restTemplate;
    private final Supplier<OffsetDateTime> timeSupplier;

    public RadioBrowserAdapter(@Value("${integration.radio-browser-api-url}") String radioBrowserApiUrl,
                               Supplier<OffsetDateTime> timeSupplier) {

        restTemplate = new RestTemplateBuilder()
                .rootUri(radioBrowserApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.timeSupplier = timeSupplier;
    }

    public List<RadioStation> getRadioStations(int limit) {
        OffsetDateTime timestamp = timeSupplier.get();
        ResponseEntity<RadioBrowserStation[]> result = restTemplate.getForEntity("/json/stations?limit=" + limit, RadioBrowserStation[].class);
        RadioBrowserStation[] radioBrowserStations = result.getBody();
        return Optional.ofNullable(radioBrowserStations)
                .stream()
                .flatMap(Arrays::stream)
                .map(s -> new RadioStation(null, s.name().trim(), s.country(), timestamp))
                .toList();
    }
}

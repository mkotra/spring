package pl.mkotra.spring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.mkotra.spring.model.RadioStation;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

@Component
public class RadioBrowserAdapter {

    private final WebClient webClient;
    private final String radioBrowserApiUrl;
    private final Supplier<OffsetDateTime> timeSupplier;

    public RadioBrowserAdapter(@Value("${integration.radio-browser-api-url}") String radioBrowserApiUrl,
                               Supplier<OffsetDateTime> timeSupplier) {
        webClient = WebClient.builder()
                .baseUrl(radioBrowserApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.radioBrowserApiUrl = radioBrowserApiUrl;
        this.timeSupplier = timeSupplier;
    }

    public Flux<RadioStation> getRadioStations(int limit) {
        OffsetDateTime timestamp = timeSupplier.get();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/json/stations")
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToFlux(RadioBrowserStation.class)
                .map(s -> new RadioStation(
                        null, s.name().trim(), s.country(), timestamp
                ));
    }
}

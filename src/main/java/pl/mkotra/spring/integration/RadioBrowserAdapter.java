package pl.mkotra.spring.integration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.mkotra.spring.model.RadioStation;
import reactor.core.publisher.Flux;

@Component
public class RadioBrowserAdapter {

    private static final String URL = "https://de1.api.radio-browser.info/json";

    private final WebClient client;

    public RadioBrowserAdapter() {
        client = WebClient.builder()
                .baseUrl(URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Flux<RadioStation> find(int limit) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stations")
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToFlux(RadioBrowserStation.class)
                .map(s -> new RadioStation(
                        null, s.name().trim(), s.country()
                ));
    }
}

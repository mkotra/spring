package pl.mkotra.spring.integration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.mkotra.spring.model.RadioStation;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;

@Component
public class RadioBrowserAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RadioBrowserAdapter.class);

    private final Supplier<OffsetDateTime> timeSupplier;
    private final RestClient restClient;
    private final Retry retry;
    private final RadioStationFactory radioStationFactory;

    RadioBrowserAdapter(Supplier<OffsetDateTime> timeSupplier,
                        RestClient restClient,
                        RetryRegistry retryRegistry,
                        RadioStationFactory radioStationFactory) {
        this.restClient = restClient;
        this.timeSupplier = timeSupplier;
        this.retry = retryRegistry.retry("radioBrowserRetry");
        this.radioStationFactory = radioStationFactory;
    }

    public List<RadioStation> getRadioStations(int limit) {
        return Retry.decorateCheckedSupplier(retry, () -> {
            logger.info("Retrieving radio stations from external service...");
            RadioBrowserStation[] radioBrowserStations = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/json/stations")
                            .queryParam("limit", limit)
                            .build())
                    .retrieve()
                    .body(RadioBrowserStation[].class);

            OffsetDateTime timestamp = timeSupplier.get();

            return Optional.ofNullable(radioBrowserStations)
                    .stream()
                    .flatMap(Arrays::stream)
                    .map(s -> radioStationFactory.create(s, timestamp))
                    .toList();
        }).unchecked().get();
    }
}

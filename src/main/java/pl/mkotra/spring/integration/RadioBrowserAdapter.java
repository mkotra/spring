package pl.mkotra.spring.integration;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.mkotra.spring.core.model.RadioStation;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;

@Component
public class RadioBrowserAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RadioBrowserAdapter.class);

    private final Supplier<OffsetDateTime> timeSupplier;
    private final RestClient restClient;
    private final Retry retry;
    private final RateLimiter rateLimiter;
    private final RadioStationFactory radioStationFactory;

    RadioBrowserAdapter(Supplier<OffsetDateTime> timeSupplier,
                        RestClient restClient,
                        RetryRegistry retryRegistry,
                        RateLimiterRegistry rateLimiterRegistry,
                        RadioStationFactory radioStationFactory) {
        this.restClient = restClient;
        this.timeSupplier = timeSupplier;
        this.retry = retryRegistry.retry("radio-browser-retry");
        this.rateLimiter = rateLimiterRegistry.rateLimiter("radio-browser-rate-limiter");
        this.radioStationFactory = radioStationFactory;
    }

    public List<RadioStation> getRadioStations(int limit) {
        Supplier<List<RadioStation>> decoratedCall = RateLimiter.decorateSupplier(rateLimiter,
                        Retry.decorateSupplier(retry, () -> makeApiCall(limit)));

        return decoratedCall.get();
    }

    private List<RadioStation> makeApiCall(int limit) {
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
    }
}

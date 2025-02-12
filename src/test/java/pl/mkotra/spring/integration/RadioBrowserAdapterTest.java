package pl.mkotra.spring.integration;

import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import pl.mkotra.spring.model.RadioStation;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RadioBrowserAdapterTest {

    private final RestClient restClient = mock(RestClient.class);
    @SuppressWarnings("unchecked")
    private final Supplier<OffsetDateTime> timeSupplier = mock(Supplier.class);
    private final RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
    private final RadioStationFactory radioStationFactory = mock(RadioStationFactory.class);

    private final RadioBrowserAdapter radioBrowserAdapter = new RadioBrowserAdapter(timeSupplier, restClient, retryRegistry, radioStationFactory);

    @Test
    void shouldReturnListOfRadioStations_whenApiReturnsData() {
        //given
        OffsetDateTime fixedTimestamp = OffsetDateTime.parse("2025-02-06T12:00:00Z");
        when(timeSupplier.get()).thenReturn(fixedTimestamp);

        RadioBrowserStation[] mockResponse = {
                new RadioBrowserStation("uuid1", "Radio 1", "USA", "http://www.example.com/test.pls", "jazz,pop,rock,indie"),
                new RadioBrowserStation("uuid2", "Radio 2", "USA", "http://www.example.com/test.pls", "jazz,pop,rock,indie"),
        };
        when(radioStationFactory.create(mockResponse[0], fixedTimestamp)).thenReturn(mock(RadioStation.class));
        when(radioStationFactory.create(mockResponse[1], fixedTimestamp)).thenReturn(mock(RadioStation.class));

        mockRestClientResponse(mockResponse);

        //when
        List<RadioStation> result = radioBrowserAdapter.getRadioStations(2);

        //then
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .allSatisfy(station -> {
                    assertThat(station.id()).isNull();
                });

        verify(timeSupplier).get();
        verify(restClient).get();
        verify(radioStationFactory, times(2)).create(any(RadioBrowserStation.class), any(OffsetDateTime.class));
    }

    @SuppressWarnings("unchecked")
    private void mockRestClientResponse(RadioBrowserStation[] mockResponse) {
        var requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        var requestHeaderSpec = mock(RestClient.RequestHeadersSpec.class);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeaderSpec);
        var responseSpec = mock(RestClient.ResponseSpec.class);
        when(requestHeaderSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RadioBrowserStation[].class)).thenReturn(mockResponse);
    }
}

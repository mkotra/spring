package pl.mkotra.spring.integration;

public record RadioBrowserStation(
        String stationuuid,
        String name,
        String country,
        String url,
        String tags) {
}

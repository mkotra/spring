package pl.mkotra.spring.model;

import java.time.OffsetDateTime;

public record RadioStation(
        String id,
        String uuid,
        String name,
        String country,
        OffsetDateTime timestamp) {
}

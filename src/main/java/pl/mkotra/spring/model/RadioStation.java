package pl.mkotra.spring.model;

import java.time.OffsetDateTime;
import java.util.List;

public record RadioStation(
        String id,
        String uuid,
        String name,
        String country,
        String url,
        List<String> tags,
        OffsetDateTime timestamp) {
}

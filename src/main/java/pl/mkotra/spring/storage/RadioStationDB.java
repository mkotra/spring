package pl.mkotra.spring.storage;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "radioStations")
@TypeAlias("RadioStationDB")
public record RadioStationDB(
        String id,
        String uuid,
        String name,
        String country,
        String url,
        List<String> tags,
        Instant timestamp)  {
}

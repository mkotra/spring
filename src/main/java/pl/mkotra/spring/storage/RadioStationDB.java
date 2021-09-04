package pl.mkotra.spring.storage;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "radioStations")
@TypeAlias("RadioStationDB")
public record RadioStationDB(
        String id,
        String name,
        String country)  {
}

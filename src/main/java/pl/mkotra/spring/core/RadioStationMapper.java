package pl.mkotra.spring.core;


import pl.mkotra.spring.core.RadioStationMapperImpl;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

interface RadioStationMapper {
    RadioStationMapper INSTANCE = new RadioStationMapperImpl();
    RadioStation map(RadioStationDB item);
    RadioStationDB map(RadioStation item);

    default Instant map(OffsetDateTime timestamp) {
        return timestamp.toInstant().truncatedTo(ChronoUnit.MILLIS);
    }

    default OffsetDateTime map(Instant timestamp) {
        return OffsetDateTime.ofInstant(timestamp.truncatedTo(ChronoUnit.MILLIS), ZoneId.of("UTC"));
    }
}

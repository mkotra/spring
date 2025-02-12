package pl.mkotra.spring.core;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.mkotra.spring.core.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Mapper
interface RadioStationMapper {
    RadioStationMapper INSTANCE = Mappers.getMapper(RadioStationMapper.class);
    RadioStation map(RadioStationDB item);
    RadioStationDB map(RadioStation item);

    default Instant map(OffsetDateTime timestamp) {
        return timestamp.toInstant().truncatedTo(ChronoUnit.MILLIS);
    }

    default OffsetDateTime map(Instant timestamp) {
        return OffsetDateTime.ofInstant(timestamp.truncatedTo(ChronoUnit.MILLIS), ZoneId.of("UTC"));
    }
}

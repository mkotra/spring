package pl.mkotra.spring.core;

import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;

import javax.annotation.processing.Generated;
import java.time.Instant;
import java.time.OffsetDateTime;

class RadioStationMapperImpl implements RadioStationMapper {

    @Override
    public RadioStation map(RadioStationDB item) {
        if ( item == null ) {
            return null;
        }

        String id = null;
        String uuid = null;
        String name = null;
        String country = null;
        OffsetDateTime timestamp = null;

        id = item.id();
        uuid = item.uuid();
        name = item.name();
        country = item.country();
        timestamp = map( item.timestamp() );

        RadioStation radioStation = new RadioStation( id, uuid, name, country, timestamp );

        return radioStation;
    }

    @Override
    public RadioStationDB map(RadioStation item) {
        if ( item == null ) {
            return null;
        }

        String id = null;
        String uuid = null;
        String name = null;
        String country = null;
        Instant timestamp = null;

        id = item.id();
        uuid = item.uuid();
        name = item.name();
        country = item.country();
        timestamp = map( item.timestamp() );

        RadioStationDB radioStationDB = new RadioStationDB( id, uuid, name, country, timestamp );

        return radioStationDB;
    }
}

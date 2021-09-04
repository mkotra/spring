package pl.mkotra.spring.core;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;

@Mapper
interface RadioStationMapper {
    RadioStationMapper INSTANCE = Mappers.getMapper(RadioStationMapper.class);
    RadioStation map(RadioStationDB item);
    RadioStationDB map(RadioStation item);
}

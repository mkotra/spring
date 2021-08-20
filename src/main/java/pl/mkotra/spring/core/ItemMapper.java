package pl.mkotra.spring.core;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.mkotra.spring.model.Item;
import pl.mkotra.spring.storage.ItemDB;

@Mapper
interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);
    Item map(ItemDB item);
    ItemDB map(Item item);
}

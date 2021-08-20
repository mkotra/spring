package pl.mkotra.spring.storage;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
@TypeAlias("ItemDB")
public record ItemDB(String id, String name)  {

}

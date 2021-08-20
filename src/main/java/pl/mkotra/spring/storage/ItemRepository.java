package pl.mkotra.spring.storage;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<ItemDB, String> {
}
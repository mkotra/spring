package pl.mkotra.spring.storage;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadioStationRepository extends ListCrudRepository<RadioStationDB, String> {
}
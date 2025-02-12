package pl.mkotra.spring.storage;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadioStationRepository extends ListCrudRepository<RadioStationDB, String> {

    @Query("{ 'tags' : { $all: ?0 } }")
    List<RadioStationDB> findByTagsContainingAll(List<String> tags);
}
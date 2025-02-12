package pl.mkotra.spring.storage;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mkotra.spring.BaseIT;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RadioStationRepositoryIT extends BaseIT {

    @Autowired
    RadioStationRepository repository;

    @ParameterizedTest(name = "Tags: {0} → Expected ids: {1}")
    @ArgumentsSource(TestArgumentsProvider.class)
    void findByTagsContainingAll_returnsValid(List<String> tags, List<String> expectedIds) {
        //given
        prepareDB();

        //when
        List<RadioStationDB> result = repository.findByTagsContainingAll(tags);

        //then
        List<String> ids = result.stream()
                .map(RadioStationDB::id)
                .toList();

        assertThat(ids).isEqualTo(expectedIds);
    }

    private static class TestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(List.of(), List.of()),
                    Arguments.of(List.of("jazz"), List.of("id_1")),
                    Arguments.of(List.of("news"), List.of("id_2")),
                    Arguments.of(List.of("rock"), List.of("id_1", "id_3")),
                    Arguments.of(List.of("rock", "metal", "thrash"), List.of("id_3")),
                    Arguments.of(List.of("metal", "news"), List.of()),
                    Arguments.of(List.of("pop"), List.of()),
                    Arguments.of(List.of("jazz", "rock", "thrash"), List.of()),
                    Arguments.of(List.of("jazz", "rock"), List.of("id_1")),
                    Arguments.of(List.of("rock", "metal", "thrash", "jazz"), List.of()),
                    Arguments.of(List.of("ROCK"), List.of())
            );
        }
    }

    private void prepareDB() {
        repository.deleteAll();
        repository.saveAll(List.of(
                new RadioStationDB("id_1", "uuid_1", "Radio 1", "Poland", "url1", List.of("jazz", "rock"), Instant.now()),
                new RadioStationDB("id_2", "uuid_2", "Radio 2", "Poland", "url2", List.of("news"), Instant.now()),
                new RadioStationDB("id_3", "uuid_3", "Radio 3", "Poland", "url3", List.of("rock", "metal", "thrash"), Instant.now())
        ));
    }
}
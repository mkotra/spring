package pl.mkotra.spring.core;

import org.springframework.stereotype.Service;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationRepository;
import pl.mkotra.spring.storage.RadioStationDB;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RadioStationService {

    private final RadioStationRepository radioStationRepository;
    private final RadioBrowserAdapter radioBrowserAdapter;

    public RadioStationService(RadioStationRepository radioStationRepository,
                               RadioBrowserAdapter radioBrowserAdapter) {
        this.radioStationRepository = radioStationRepository;
        this.radioBrowserAdapter = radioBrowserAdapter;
    }

    public Flux<RadioStation> pull(int limit) {
        Flux<RadioStation> radioStationFlux = radioBrowserAdapter.getRadioStations(limit);
        radioStationRepository.deleteAll();
        return radioStationFlux.collectList().flatMapMany(this::save);
    }

    public Flux<RadioStation> findAll() {
        return radioStationRepository.findAll().map(RadioStationMapper.INSTANCE::map);
    }

    public Mono<RadioStation> save(RadioStation radioStation) {
        Mono<RadioStationDB> saved = radioStationRepository.save(RadioStationMapper.INSTANCE.map(radioStation));
        return saved.map(RadioStationMapper.INSTANCE::map);
    }

    public Flux<RadioStation> save(Collection<RadioStation> radioStations) {
        List<RadioStationDB> ratioStationDBS = radioStations.stream()
                .map(RadioStationMapper.INSTANCE::map)
                .collect(Collectors.toList());
        Flux<RadioStationDB> saved = radioStationRepository.saveAll(ratioStationDBS);
        return saved.map(RadioStationMapper.INSTANCE::map);
    }
}

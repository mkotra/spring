package pl.mkotra.spring.core;

import org.springframework.stereotype.Service;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.core.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RadioStationService {

    private final RadioStationRepository radioStationRepository;
    private final RadioBrowserAdapter radioBrowserAdapter;

    public RadioStationService(RadioStationRepository radioStationRepository,
                               RadioBrowserAdapter radioBrowserAdapter) {
        this.radioStationRepository = radioStationRepository;
        this.radioBrowserAdapter = radioBrowserAdapter;
    }

    public List<RadioStation> pull(int limit) {
        List<RadioStation> radioStations = radioBrowserAdapter.getRadioStations(limit);
        List<RadioStationDB> radioStationsDB = radioStations.stream()
                .map(RadioStationMapper.INSTANCE::map)
                .toList();
        radioStationRepository.deleteAll();
        return radioStationRepository.saveAll(radioStationsDB).stream()
                .map(RadioStationMapper.INSTANCE::map)
                .toList();
    }

    public List<RadioStation> findAll() {
        return radioStationRepository.findAll().stream()
                .map(RadioStationMapper.INSTANCE::map)
                .toList();
    }

    public List<RadioStation> findByTags(List<String> tags) {
        return radioStationRepository.findByTagsContainingAll(tags).stream()
                .map(RadioStationMapper.INSTANCE::map)
                .toList();
    }

    public Optional<RadioStation> find(String id) {
        return radioStationRepository.findById(id)
                .map(RadioStationMapper.INSTANCE::map);
    }
}

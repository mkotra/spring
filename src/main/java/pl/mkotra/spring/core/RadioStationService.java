package pl.mkotra.spring.core;

import org.springframework.stereotype.Service;
import pl.mkotra.spring.integration.KafkaProducer;
import pl.mkotra.spring.integration.RadioBrowserAdapter;
import pl.mkotra.spring.model.RadioStation;
import pl.mkotra.spring.storage.RadioStationDB;
import pl.mkotra.spring.storage.RadioStationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RadioStationService {

    private final RadioStationRepository radioStationRepository;
    private final RadioBrowserAdapter radioBrowserAdapter;
    private final KafkaProducer kafkaProducer;

    public RadioStationService(RadioStationRepository radioStationRepository,
                               RadioBrowserAdapter radioBrowserAdapter,
                               KafkaProducer kafkaProducer) {
        this.radioStationRepository = radioStationRepository;
        this.radioBrowserAdapter = radioBrowserAdapter;
        this.kafkaProducer = kafkaProducer;
    }

    public List<RadioStation> pull(int limit) {
        List<RadioStation> radioStations = radioBrowserAdapter.getRadioStations(limit);
        radioStationRepository.deleteAll();
        List<RadioStationDB> radioStationsDB = radioStations.stream()
                .map(RadioStationMapper.INSTANCE::map)
                .toList();
        List<RadioStation> saved = radioStationRepository.saveAll(radioStationsDB).stream()
                .map(RadioStationMapper.INSTANCE::map)
                .toList();

        kafkaProducer.sendMessage(saved.size());
        return saved;
    }

    public List<RadioStation> findAll() {
        return radioStationRepository.findAll().stream()
                .map(RadioStationMapper.INSTANCE::map)
                .toList();
    }

    public Optional<RadioStation> find(String id) {
        return radioStationRepository.findById(id)
                .map(RadioStationMapper.INSTANCE::map);
    }
}

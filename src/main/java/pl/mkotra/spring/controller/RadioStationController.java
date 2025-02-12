package pl.mkotra.spring.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.model.RadioStation;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/radio-stations")
public class RadioStationController {

    private static final Logger logger = LoggerFactory.getLogger(RadioStationController.class);

    private final RadioStationService radioStationService;
    private final MeterRegistry meterRegistry;
    private final Timer timer;

    public RadioStationController(RadioStationService radioStationService, MeterRegistry meterRegistry) {
        this.radioStationService = radioStationService;
        this.meterRegistry = meterRegistry;
        this.timer = Timer.builder("pull_radio_stations_time")
                .description("Time taken for pull operation")
                .register(meterRegistry);
    }

    @PostMapping("/pull")
    public Collection<RadioStation> pull(@RequestParam @Max(1000) int limit) {
        logger.info("Pulling radio stations using thread: {}", Thread.currentThread());

        meterRegistry.counter("pull_radio_stations_count").increment();

        return timer.record(() -> radioStationService.pull(limit));
    }

    @GetMapping
    public ResponseEntity<Collection<RadioStation>> getAll() {
        logger.info("Getting radio stations using thread: {}", Thread.currentThread());

        return ResponseEntity.ok(radioStationService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<RadioStation>> search(@RequestParam(value = "tags", required = false) @NotEmpty List<String> tags) {
        logger.info("Searching radio stations using thread: {}", Thread.currentThread());

        return ResponseEntity.ok(radioStationService.findByTags(tags));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RadioStation> get(@PathVariable String id) {
        logger.info("Getting radio station using thread: {}", Thread.currentThread());

        return radioStationService.find(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

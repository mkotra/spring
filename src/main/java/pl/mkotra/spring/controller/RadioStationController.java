package pl.mkotra.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.model.RadioStation;

import java.util.Collection;
import java.util.logging.Logger;

@RestController
@RequestMapping("/radio-stations")
public class RadioStationController {

    private static final Logger logger = Logger.getLogger(RadioStationController.class.getName());

    private final RadioStationService radioStationService;

    public RadioStationController(RadioStationService radioStationService) {
        this.radioStationService = radioStationService;
    }

    @PostMapping("/pull")
    public Collection<RadioStation> pull(@RequestParam int limit) {
        logger.info("Pulling radio stations using thread: " + Thread.currentThread());

        return radioStationService.pull(limit);
    }

    @GetMapping
    public ResponseEntity<Collection<RadioStation>> getAll() {
        logger.info("Getting radio stations using thread: " + Thread.currentThread());

        return ResponseEntity.ok(radioStationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RadioStation> get(String id) {
        logger.info("Getting radio station using thread: " + Thread.currentThread());

        return radioStationService.find("id")
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

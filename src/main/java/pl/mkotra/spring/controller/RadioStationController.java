package pl.mkotra.spring.controller;

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
    public Collection<RadioStation> findAll() {
        logger.info("Finding radio stations using thread: " + Thread.currentThread());

        return radioStationService.findAll();
    }
}

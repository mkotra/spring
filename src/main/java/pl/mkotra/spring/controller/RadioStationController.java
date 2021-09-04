package pl.mkotra.spring.controller;

import org.springframework.web.bind.annotation.*;
import pl.mkotra.spring.core.RadioStationService;
import pl.mkotra.spring.model.RadioStation;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/radio-stations")
public class RadioStationController {

    private final RadioStationService radioStationService;

    public RadioStationController(RadioStationService radioStationService) {
        this.radioStationService = radioStationService;
    }

    @PostMapping
    public Flux<RadioStation> pull(@RequestParam int limit) {
        return radioStationService.pull(limit);
    }

    @GetMapping
    public Flux<RadioStation> findAll() {
        return radioStationService.findAll();
    }
}

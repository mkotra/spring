package pl.mkotra.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mkotra.spring.DistributedProperties;

@RestController
public class DistributedPropertiesController {

    @Value("${distributed.property:none}")
    String value;

    @Autowired
    private DistributedProperties distributedProperties;

    @GetMapping("/getConfigFromValue")
    public String getConfigFromValue() {
        return value;
    }

    @GetMapping("/getConfigFromProperty")
    public String getConfigFromProperty() {
        return distributedProperties.getProperty();
    }
}

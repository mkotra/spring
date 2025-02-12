package pl.mkotra.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mkotra.spring.DistributedProperties;

@RestController
@RefreshScope
public class DistributedPropertiesController {

    private final DistributedProperties distributedProperties;
    private final String value;

    public DistributedPropertiesController(DistributedProperties distributedProperties,
                                           @Value("${distributed.property}") String value) {
        this.distributedProperties = distributedProperties;
        this.value = value;
    }

    @GetMapping("/config-from-value")
    public String getConfigFromValue() {
        return value;
    }

    @GetMapping("/config-from-properties")
    public String getConfigFromProperties() {
        return distributedProperties.getProperty();
    }
}

package pl.mkotra.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(properties = "spring.profiles.include=tests")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
abstract class BaseIT {

    static final int RADIO_BROWSER_TEST_API_PORT = 9999;

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:8.0.0"));

    static {
        mongo.start();
    }

    @RegisterExtension
    protected static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(RADIO_BROWSER_TEST_API_PORT))
            .build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.consul.enabled", () -> "false");
        registry.add("spring.data.mongodb.database", () -> "demo");
        registry.add("spring.data.mongodb.database", () -> "demo");
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
        registry.add("integration.radio-browser-api-url", () -> "http://localhost:" + RADIO_BROWSER_TEST_API_PORT);
        registry.add("distributed.property", () -> "dummy");
    }
}

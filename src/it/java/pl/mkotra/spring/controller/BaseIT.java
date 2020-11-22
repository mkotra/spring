package pl.mkotra.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("tests")
@Testcontainers
abstract class BaseIT {

    private static final int REDIS_PORT = 6379;
    private static final String REDIS_IMAGE = "redis:5.0.3-alpine";

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
            .withExposedPorts(REDIS_PORT);

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getContainerIpAddress);
        registry.add("spring.redis.port",  () -> redis.getMappedPort(REDIS_PORT));
    }
}

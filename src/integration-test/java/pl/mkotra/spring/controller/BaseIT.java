package pl.mkotra.spring.controller;

import org.junit.jupiter.api.AfterEach;
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
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("tests")
abstract class BaseIT {

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:4.4.8"));

    static {
        mongo.start();
    }

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build();
    }

    @AfterEach
    public void tearDown() {
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.database", () -> "demo");
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }
}

package pl.mkotra.spring.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("tests")
abstract class BaseIT {

    static final int WIREMOCK_PORT = 8443;

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:4.4.8"));

    static {
        mongo.start();
    }

    @RegisterExtension
    protected static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(WIREMOCK_PORT))
            .build();

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
        registry.add("integration.radio-browser-api-url", () -> "localhost:" + WIREMOCK_PORT);

    }
}

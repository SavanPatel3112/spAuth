package com.example.authmoduls;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
@SpringBootTest

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@Slf4j
public class AbstractContainerTest {
    @Container
    private static final MongoDBContainer CONTAINER = MongoDBContainerSingleton.getInstance();




    static {
        try {
            CONTAINER.start();
        } catch (Throwable t) {
            log.error("Failure during static initialization", t);
            throw t;
        }
    }

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", CONTAINER::getReplicaSetUrl);
    }
}

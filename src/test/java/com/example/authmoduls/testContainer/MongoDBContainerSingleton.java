package com.example.authmoduls.testContainer;

import org.testcontainers.containers.MongoDBContainer;

public class MongoDBContainerSingleton extends MongoDBContainer {
    private static final String DOCKER_IMAGE_NAME = "mongo:4.0.10";
    private static MongoDBContainerSingleton CONTAINER;

    private MongoDBContainerSingleton() {
        super(DOCKER_IMAGE_NAME);
    }

    public static MongoDBContainerSingleton getInstance() {
        if (CONTAINER == null) {
            CONTAINER = new MongoDBContainerSingleton();
        }
        return CONTAINER;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}

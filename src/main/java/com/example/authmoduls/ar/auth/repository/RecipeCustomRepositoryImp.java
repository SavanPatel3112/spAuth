package com.example.authmoduls.ar.auth.repository;

import org.springframework.data.mongodb.core.MongoTemplate;

public class RecipeCustomRepositoryImp implements RecipeCustomRepository {

    private final MongoTemplate mongoTemplate;

    public RecipeCustomRepositoryImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


}

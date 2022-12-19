package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.model.RecipeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends MongoRepository<RecipeModel,String>,RecipeCustomRepository{

    Optional<RecipeModel> findByIdAndSoftDeleteIsFalse(String id);
     List<RecipeModel> findAllBySoftDeleteFalse();


}

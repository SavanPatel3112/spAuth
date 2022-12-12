package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.ar.auth.model.Shopping;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShoppingRepository extends MongoRepository<Shopping,String>,ShoppingCustomRepository{
    Optional<Shopping> findByIdAndSoftDeleteIsFalse(String id);

}

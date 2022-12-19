package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.RecipeFilter;
import com.example.authmoduls.ar.auth.decorator.RecipeSortBy;
import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends MongoRepository<RecipeModel,String>,RecipeCustomRepository{

    Optional<RecipeModel> findByIdAndSoftDeleteIsFalse(String id);
     List<RecipeModel> findAllBySoftDeleteFalse();


}

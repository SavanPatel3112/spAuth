package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface RecipeService {
    RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id) throws InvocationTargetException, IllegalAccessException;

    List<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException;

    RecipeResponse getRecipe(String id) throws InvocationTargetException, IllegalAccessException;

    void deleteRecipe(String id);
}

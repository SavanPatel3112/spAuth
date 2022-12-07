package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.common.enums.Role;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface RecipeService {
    RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id , Role role) throws InvocationTargetException, IllegalAccessException;

    List<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException;

    RecipeResponse getRecipe(String id) throws InvocationTargetException, IllegalAccessException;

    void deleteRecipe(String id);

    void recipeUpdate(String id, Role role , RecipeAddRequest recipeAddRequest) throws InvocationTargetException, IllegalAccessException;

}

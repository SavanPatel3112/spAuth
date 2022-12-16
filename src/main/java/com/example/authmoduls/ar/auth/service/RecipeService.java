package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.decorator.RecipeAddRequest;
import com.example.authmoduls.ar.auth.decorator.RecipeResponse;
import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import com.example.authmoduls.auth.model.Accesss;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface RecipeService {
    RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id , Accesss accesss) throws InvocationTargetException, IllegalAccessException;

    List<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException;

    RecipeResponse getRecipe(String id) throws InvocationTargetException, IllegalAccessException;

    void deleteRecipe(String id);

    void recipeUpdate(String id, Accesss accesss , RecipeAddRequest recipeAddRequest) throws InvocationTargetException, IllegalAccessException;

    void shoppingList(String id, String loginID);

    List<ShoppingListLog>  getRecipeList(String loginId);

}

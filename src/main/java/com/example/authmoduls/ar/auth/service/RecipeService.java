package com.example.authmoduls.ar.auth.service;

import com.example.authmoduls.ar.auth.decorator.*;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public interface RecipeService {
    RecipeResponse addOrUpdateRecipe(RecipeAddRequest recipeAddRequest, String id , Accesss accesss) throws InvocationTargetException, IllegalAccessException;

    List<RecipeResponse> getAllRecipe() throws InvocationTargetException, IllegalAccessException;

    RecipeResponse getRecipe(String id) throws InvocationTargetException, IllegalAccessException;

    void deleteRecipe(String id);

    void recipeUpdate(String id, Accesss accesss , RecipeAddRequest recipeAddRequest) throws InvocationTargetException, IllegalAccessException;

    ShoppingListLog shoppingList(String id, String loginID);

    List<ShoppingListLog>  getRecipeList(String loginId);

    Page<RecipeModel> getAllRecipeByFilterAndSortAndPage(RecipeFilter recipeFilter, FilterSortRequest.SortRequest<RecipeSortBy> sort, PageRequest pagination);

}

package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.RecipeFilter;
import com.example.authmoduls.ar.auth.decorator.RecipeSortBy;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RecipeCustomRepository {

    /*Page<RecipeModel> findAllRecipeByFilterAndSortAndPage(RecipeFilter recipeFilter, FilterSortRequest.SortRequest<RecipeSortBy> sort, PageRequest pagination);*/
}

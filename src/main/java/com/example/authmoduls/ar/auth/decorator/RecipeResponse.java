package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {
    String id;
    String itemName;
    String itemUrl;
    String itemDescription;
    List<RecipeIngredient> recipeIngredient;

    @JsonIgnore
    boolean softDelete = false;

}

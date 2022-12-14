package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListLog {

    String recipeId;
    String loginId;
    List<RecipeIngredient> ingredients;
    Date data;
    @JsonIgnore
    boolean softDelete = false;    
    
    
}

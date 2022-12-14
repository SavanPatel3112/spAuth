package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingListLog {

    String recipeId;
    String loginId;
    String itemName;
    List<RecipeIngredient> ingredients;
    Date date;
    @JsonIgnore
    boolean softDelete = false;    
    
    
}

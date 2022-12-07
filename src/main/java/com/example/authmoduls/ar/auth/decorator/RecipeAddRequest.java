package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.example.authmoduls.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeAddRequest {

    String itemName;
    String itemUrl;
    String itemDescription;
    List<RecipeIngredient> recipeIngredient;
}

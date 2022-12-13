package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.RecipeIngredient;
import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingResponse {

    String id;
    List<RecipeIngredient> name;
    Date data = new Date();
    Accesss accesss;
    @JsonIgnore
    String token;
    @JsonIgnore
    boolean softDelete = false;

}

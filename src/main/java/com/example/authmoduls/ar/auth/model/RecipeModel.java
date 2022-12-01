package com.example.authmoduls.ar.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "recipe")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RecipeModel {
    @Id
    String id;
    String itemName;
    String itemUrl;
    String itemDescription;
    List<RecipeIngredient> recipeIngredient;
    Date data = new Date();
    @JsonIgnore
    boolean softDelete = false;

}

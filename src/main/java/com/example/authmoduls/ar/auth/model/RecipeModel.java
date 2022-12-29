package com.example.authmoduls.ar.auth.model;

import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Document(collection = "recipe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeModel {

    @Id
    String id;
    String itemName;
    String itemUrl;
    String itemDescription;
    List<RecipeIngredient> recipeIngredient;
    String date;
    Accesss accesss;
    String  loginId;
    @JsonIgnore
    boolean softDelete = false;

}

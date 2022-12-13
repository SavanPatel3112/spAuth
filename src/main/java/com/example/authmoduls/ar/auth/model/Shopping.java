package com.example.authmoduls.ar.auth.model;

import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "shopping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shopping {
    @Id
    String id;
    List<RecipeIngredient> name;
    Date data = new Date();
    Accesss accesss;
    @JsonIgnore
    boolean softDelete = false;
}

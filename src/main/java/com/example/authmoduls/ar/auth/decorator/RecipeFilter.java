package com.example.authmoduls.ar.auth.decorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilter {

    String search;
    String itemName;
    String itemDescription;
    Set<String> id;
    @JsonIgnore
    boolean softDelete = false;

    public String getSearch(){
        if(search !=null){
            return search.trim();
        }
        return search;
    }

}

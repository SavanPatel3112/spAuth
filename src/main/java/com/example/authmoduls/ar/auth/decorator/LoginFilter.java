package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.auth.model.Accesss;
import com.fasterxml.jackson.annotation.JsonIgnore;
import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginFilter {

    String search;
    Accesss accesss;
    Gender gender;
    String id;
    @JsonIgnore
    boolean softDelete = false;

    public String getSearch(){
        if(search !=null){
            return search.trim();
        }
        return search;
    }

}

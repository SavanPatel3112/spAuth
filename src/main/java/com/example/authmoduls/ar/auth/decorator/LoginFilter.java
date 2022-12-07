package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.cc.enums.Plan;
import com.example.authmoduls.common.enums.Role;
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
    Role role;
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
    public Role getRole(){
        return role;
    }
    public Gender getGender(){
        return gender;
    }

}

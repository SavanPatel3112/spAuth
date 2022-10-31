package com.example.authmoduls.auth.decorator;

import com.example.authmoduls.cc.enums.Plan;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserFilter {
    String search;
    Role role;
    Plan plan;
    String Id;


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
    public Plan getPlan(){
        return plan;
    }

}

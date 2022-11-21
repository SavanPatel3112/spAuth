package com.example.authmoduls.auth.decorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Getter
@NoArgsConstructor
public enum UserDataSortBy {
    FULL_NAME("getFullName");
    @JsonIgnore
    private String value;
    UserDataSortBy(String value) {
        this.value = value;
    }

    Map<String,String> toMap() {
        Map<String,String> map=new HashMap<>();
        map.put("value",this.toString());
        return map;
    }}

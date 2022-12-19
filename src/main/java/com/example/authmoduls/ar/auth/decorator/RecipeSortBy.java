package com.example.authmoduls.ar.auth.decorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public enum RecipeSortBy {


    ITEMNAME("itemName"),
    ITEMDESCRIPTION("itemDescription");

    @JsonIgnore
    private String value;

    RecipeSortBy(String value) {
        this.value = value;
    }

    Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("value", this.toString());
        return map;
    }

}

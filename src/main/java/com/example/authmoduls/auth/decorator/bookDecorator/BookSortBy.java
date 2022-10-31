package com.example.authmoduls.auth.decorator.bookDecorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor

public enum BookSortBy {
 MONTH("month"),
     YEAR("year");

    @JsonIgnore
    private String value;
    BookSortBy(String value) {
        this.value = value;
    }
    Map<String,String> toMap() {
        Map<String,String> map=new HashMap<>();
        map.put("value",this.toString());
        return map;
    }}

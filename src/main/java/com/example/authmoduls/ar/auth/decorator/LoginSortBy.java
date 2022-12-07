package com.example.authmoduls.ar.auth.decorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Getter
@NoArgsConstructor
public enum LoginSortBy {

    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    MIDDLE_NAME("middleName"),
    EMAIL("email");

    @JsonIgnore
    private String value;

    LoginSortBy(String value) {
        this.value = value;
    }

    Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("value", this.toString());
        return map;
    }

}

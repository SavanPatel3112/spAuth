package com.example.authmoduls.ar.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private String value;

    Gender(String value) {
        this.value = value;
    }

}

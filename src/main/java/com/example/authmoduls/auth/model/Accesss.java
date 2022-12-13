package com.example.authmoduls.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum Accesss {

    USER("User"), ADMIN("Admin"), ANONYMOUS("Anonymous");

    private String value;

    Accesss(String value) {
        this.value = value;
    }

}

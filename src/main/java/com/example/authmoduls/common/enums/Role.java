package com.example.authmoduls.common.enums;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum Role {

    STUDENT("Student"), DEPARTMENT("Department"),USER("User"), ADMIN("Admin"), ANONYMOUS("Anonymous"), SYSTEM("System");

    private String value;

    Role(String value) {
        this.value = value;
    }
}

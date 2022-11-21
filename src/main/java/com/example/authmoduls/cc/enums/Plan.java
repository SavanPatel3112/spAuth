package com.example.authmoduls.cc.enums;

public enum Plan {
    LIFETIME("LifeTime"), ANNUAL("Annual"), MONTHLY("Monthly");
    private final String value;

    Plan(String value) {
        this.value = value;
    }
}

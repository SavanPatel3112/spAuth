package com.example.authmoduls.auth.decorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groovy.transform.BaseScript;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resultupdate {
    double spi;
    @JsonIgnore
    Date date;
    int year;
}

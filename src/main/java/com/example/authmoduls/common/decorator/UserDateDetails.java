package com.example.authmoduls.common.decorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDateDetails {
    Set<String> userIds;
    int year;
    double month;
    double count;

}

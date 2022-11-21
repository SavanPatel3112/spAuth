package com.example.authmoduls.common.decorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChartDetails {
    Set<String> userIds;
    int year;
    int month;
}

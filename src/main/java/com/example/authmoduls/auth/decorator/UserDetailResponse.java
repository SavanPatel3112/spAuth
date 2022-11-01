package com.example.authmoduls.auth.decorator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {
    String firstName;
    String middleName;
    String lastName;
    String email;
    String mobileNo;

    double cgpa;
    List<Result> results;
    //Result results;
}

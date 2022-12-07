package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.auth.model.Address;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    String id;
    String firstName;
    String middleName;
    String lastName;
    String fullName;
    String email;
    String passWord;
    Role role;
    Gender gender;
    String token;
    @JsonIgnore
    Date date;
    @JsonIgnore
    boolean softDelete = false;
    @JsonIgnore
    Date loginTime;
    @JsonIgnore
    Date logOutTime;

}

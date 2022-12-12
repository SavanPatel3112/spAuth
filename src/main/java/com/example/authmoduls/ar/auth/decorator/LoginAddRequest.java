package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.ar.auth.model.Gender;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAddRequest {

    String firstName;
    String middleName;
    String lastName;
    String email;
    String passWord;
    String confirmPassword;
    @JsonIgnore
    Date date;


}

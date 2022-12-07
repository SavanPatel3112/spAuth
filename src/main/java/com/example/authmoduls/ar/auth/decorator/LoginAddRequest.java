package com.example.authmoduls.ar.auth.decorator;

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
    String fullName;
    String email;
    String passWord;
    String confirmPassword;
    @JsonIgnore
    Date date;


}

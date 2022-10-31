package com.example.authmoduls.auth.decorator;

import com.example.authmoduls.auth.model.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddRequest {
    String id;
    String firstName;
    String middleName;
    String lastName;
    String email;
    String userName;
    String password;
    Address address;
    String confirmPassword;
    String newPassword;
    String mobileNo;
    @JsonFormat(pattern="yyyy-MM-dd")
    Date birthDate;
}

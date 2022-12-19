package com.example.authmoduls.ar.auth.decorator;

import com.example.authmoduls.auth.model.Accesss;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTokenResponse {
    @JsonIgnore
    String token;
    Accesss accesss;
    String otp;
}

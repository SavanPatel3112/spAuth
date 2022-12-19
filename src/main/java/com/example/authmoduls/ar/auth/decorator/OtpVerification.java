package com.example.authmoduls.ar.auth.decorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerification {
    String email;
    String otp;
}

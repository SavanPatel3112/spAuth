package com.example.authmoduls.cc.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    String address;
    String city;
    String state;
    String zipCode;
}

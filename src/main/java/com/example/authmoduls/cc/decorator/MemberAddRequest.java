package com.example.authmoduls.cc.decorator;

import com.example.authmoduls.cc.modal.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddRequest {

    String firstName;
    String middleName;
    String lastName;
    String fullName;
    String email;
    String phoneNumber;
    Address address;
    @JsonIgnore
    Date date;
}
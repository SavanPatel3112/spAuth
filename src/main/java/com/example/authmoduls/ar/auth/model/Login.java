package com.example.authmoduls.ar.auth.model;

import com.example.authmoduls.auth.model.Accesss;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Document(collection = "userData")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {
    @Id
    String id;
    String firstName;
    String middleName;
    String lastName;
    String fullName;
    String email;
    String passWord;
    /*String confirmPassword;*/
    Accesss accesss;
    Gender gender;
    String token;
    String otp;
    Date otpSendTime;
    @JsonIgnore
    boolean login = false;
    @JsonIgnore
    Date date;
    @JsonIgnore
    boolean softDelete = false;
    @JsonIgnore
    Date loginTime;
    @JsonIgnore
    Date logoutTime;

    public void setFullName(){
        this.firstName = StringUtils.normalizeSpace(this.firstName);
        this.middleName = StringUtils.normalizeSpace(this.middleName);
        this.lastName = StringUtils.normalizeSpace(this.lastName);
        List<String> fullNameList = new LinkedList<>();
        fullNameList.add(firstName);
        fullNameList.add(middleName);
        fullNameList.add(lastName);
        //loop over the full name list
        //check the element of list is empty or not
        //if not empty then add element to the variable
        StringBuilder name1 = new StringBuilder();
        for (String fullName : fullNameList) {
            if (!StringUtils.isEmpty(fullName)) {
                name1.append(fullName).append(" ");
            }
        }
        String[] names = name1.toString().split(" ");
        this.fullName = name1.toString();
        if (names.length == 1) {
            firstName = names[0];
        } else if (names.length == 2) {
            firstName = names[0];
            lastName = names[1];
        } else if (names.length == 3) {
            firstName = names[0];
            middleName = names[1];
            lastName = names[2];
        } else if (names.length > 3) {
            firstName = names[0];
            middleName = names[1];
            StringBuilder name = new StringBuilder();
            for (String value : names) {
                if (!value.equals(firstName) && !value.equals(middleName)) {
                    name.append(" ").append(value);
                }
            }
            lastName = name.toString().trim();
        }
    }
}

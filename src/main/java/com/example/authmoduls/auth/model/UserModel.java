package com.example.authmoduls.auth.model;

import com.example.authmoduls.auth.decorator.Result;
import com.example.authmoduls.auth.enums.UserStatus;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.Set;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {

    @Id
    String id;
    String firstName;
    String middleName;
    String lastName;
    Integer age;
    String email;
    String userName;
    String passWord;
    String confirmPassWord;
    String newPassWord;
    List<Address> address;
    Role role;
    String fullName;
    String otp;
    String mobileNo;
    Set<String> userId;
    String createdBy;
    UserStatus userStatus;
    String city;
    String state;
    String address1;
    String token;
    Date ImportDate;
    String importedId;
    double balance;
    boolean duplicateEmail = false;
    boolean emptyEmail = false;
    boolean importFromExcel = false;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date birthDate;
    @JsonIgnore
    Date date;
    @JsonIgnore
    Double cgpi;
    @JsonIgnore
    Date loginTime;
    @JsonIgnore
    Date logoutTime;
    @JsonIgnore
    Date otpSendTime;
    @JsonIgnore
    boolean login = false;
    @JsonIgnore
    boolean softDelete = false;
    Result result;
    List<Result> results;

    public void setFullName() {

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


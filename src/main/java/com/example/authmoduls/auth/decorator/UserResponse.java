package com.example.authmoduls.auth.decorator;

import com.example.authmoduls.auth.decorator.bookDecorator.BookPurchaseLog;
import com.example.authmoduls.auth.model.Address;
import com.example.authmoduls.auth.model.Book;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    String id;
    String firstName;
    String middleName;
    String lastName;
    String otp;
    int age;
    String email;
    String userName;
    String password;
    List<Address> address;
    Role role;
    String fullName;
    String mobileNo;
    @JsonFormat(pattern="yyyy-MM-dd")
    Date birthDate;
    @JsonIgnore
    String token;
    double balance;
    BookPurchaseLog bookPurchase;
    Book book;
    List<Result> results;
    Result result;
    Set<String> userId;
    @JsonIgnore
    boolean softDelete =false;
}

package com.example.authmoduls.auth.decorator.bookDecorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groovy.transform.BaseScript;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBookDetails {
    @Id
    String id;
    String userId;
    String bookId;
    String bookName;
    double discount;
    double refundDiscount;
    String balance;
    double price;
    Date date;
    String fullName;
    @JsonIgnore
    boolean softDelete = false;


}

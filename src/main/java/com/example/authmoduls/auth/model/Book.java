package com.example.authmoduls.auth.model;

import com.example.authmoduls.auth.decorator.bookDecorator.BookPurchaseLog;
import com.example.authmoduls.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection= "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    String id;
    String bookName;
    String bookDescription;
    String authorName;
    double price;
    double discount;
    double refundDiscount;
    Role role;
    @JsonIgnore
    Date date;
    String email;
    List<BookPurchaseLog> bookPurchaseLogs;
    @JsonIgnore
    boolean softDelete = false;

}

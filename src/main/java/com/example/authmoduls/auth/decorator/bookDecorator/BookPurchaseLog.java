package com.example.authmoduls.auth.decorator.bookDecorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookPurchaseLog {

    String userId;
    String bookId;
    String bookName;
    double discount;
    double refundDiscount;
    String balance;
    double price;
    Date date;
    boolean resale;
    @JsonIgnore
    boolean softDelete = false;

}

package com.example.authmoduls.auth.decorator.bookDecorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookDetailsList {

    String bookId;
    int price;
    int count;
    String bookName;
    String fullName;


}

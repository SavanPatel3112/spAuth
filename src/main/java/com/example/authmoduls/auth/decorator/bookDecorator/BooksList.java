package com.example.authmoduls.auth.decorator.bookDecorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BooksList {

    String userId;
    String bookId;
    int price;
    int count;
    String bookName;
    String fullName;
}

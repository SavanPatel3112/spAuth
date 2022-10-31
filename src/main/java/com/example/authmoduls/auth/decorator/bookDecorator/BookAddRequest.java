package com.example.authmoduls.auth.decorator.bookDecorator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookAddRequest {
    String id;
    String bookName;
    String bookDescription;
    String authorName;
    String price;
    double discount;
}

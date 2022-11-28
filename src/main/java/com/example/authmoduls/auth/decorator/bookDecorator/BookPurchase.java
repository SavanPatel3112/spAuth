package com.example.authmoduls.auth.decorator.bookDecorator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookPurchase {
    int _id;
    List<BookData> bookData;
    int totalCount;

}
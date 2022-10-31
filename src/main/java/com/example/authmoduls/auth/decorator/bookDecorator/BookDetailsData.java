package com.example.authmoduls.auth.decorator.bookDecorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookDetailsData {
    String bookName;
    List<BookDetails> bookData;
    double count;
}

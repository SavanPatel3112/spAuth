package com.example.authmoduls.auth.decorator.bookDecorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookPurchaseDetail {

    List<BookPurchase> bookDataResponse;
    Set<String> title;
    int totalCount;

}

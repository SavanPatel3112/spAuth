package com.example.authmoduls.auth.decorator.bookDecorator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookData {

    String bookName;
    double count;

}

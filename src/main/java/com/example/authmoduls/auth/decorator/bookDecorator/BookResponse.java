package com.example.authmoduls.auth.decorator.bookDecorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    String id;
    String bookName;
    String bookDescription;
    String authorName;
    String price;
    double discount;
    @JsonIgnore
    boolean softDelete =false;

}

package com.example.authmoduls.auth.decorator.bookDecorator;

import com.example.authmoduls.auth.decorator.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class  UserBookResponseExcel {

    double price;
    String date;
    String bookName;
    double count;

    @ExcelField(excelHeader="BOOK NAME",position = 10)
    public String getBookName() {
        return bookName;
    }
    @ExcelField(excelHeader="DATE",position = 20)
    public String getDate() {
        return date;
    }
    @ExcelField(excelHeader="PRICE",position = 30)
    public double getPrice() {
        return price;
    }
    @ExcelField(excelHeader="COUNT",position = 40)
    public double getCount() {
        return count;
    }
}

package com.example.authmoduls.auth.decorator.bookDecorator;

import com.example.authmoduls.auth.decorator.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDataResponseExcel {
    @ExcelField(excelHeader="USER ID",position = 40)
    public String getUserId() {return userId;}
    @ExcelField(excelHeader="FULL NAME",position = 50)
    public String getFullName() {
        return fullName;
    }
    @ExcelField(excelHeader="BOOK ID",position = 30)
    public String getBookId() {
        return bookId;
    }
    @ExcelField(excelHeader="BOOK NAME",position = 60)
    public String getBookName() {
        return bookName;
    }
    @ExcelField(excelHeader="PRICE",position = 10)
    public double getPrice() {
        return price;
    }
    @ExcelField(excelHeader="COUNT",position = 20)
    public double getCount() {
        return count;
    }
    double price;
    double count;
    String bookId;
    String userId;
    String fullName;
    String bookName;
}
package com.example.authmoduls.auth.decorator.bookDecorator;
import com.example.authmoduls.auth.decorator.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseExcel {
    double price;
    Date date;
    String userId;
    String fullName;
    @ExcelField(excelHeader="USERID",position = 10)
    public  String getUserID(){
        return userId;
    }
    @ExcelField(excelHeader="FULL NAME",position = 20)
    public  String getFullName(){
        return fullName;
    }
    @ExcelField(excelHeader="DATE",position = 30)
    public Date getDate(){
        return  date;
    }
    @ExcelField(excelHeader="PRICE",position = 40)
    public double getPrice(){
        return  price;
    }


}

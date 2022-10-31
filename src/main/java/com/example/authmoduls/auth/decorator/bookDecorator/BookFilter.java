package com.example.authmoduls.auth.decorator.bookDecorator;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookFilter {


    @JsonIgnore
    boolean softDelete = false;
    String bookId;
    String search;
    int month;
    int  year;

    public String getSearch(){
        if(search !=null){
            return search.trim();
        }
        return search;
    }

}

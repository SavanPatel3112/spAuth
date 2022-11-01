package com.example.authmoduls.auth.decorator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
 int semester;
 double spi;
 Date date;
 int year;
 String status;
}

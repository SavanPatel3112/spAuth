package com.example.authmoduls.employee.decorator;

import com.example.authmoduls.employee.enums.GivenFrom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmplDebtResponse {
    String  amount;
    String date;
    int installment;
    String guarantor;
    String approvedBy;
    GivenFrom givenFrom;
}

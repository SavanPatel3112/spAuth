package com.example.authmoduls.employee.decorator;

import com.example.authmoduls.employee.enums.Designation;
import com.example.authmoduls.employee.enums.MaritalStatus;
import com.example.authmoduls.employee.enums.ResidenceStatus;
import com.example.authmoduls.employee.enums.WorkDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeResponse {
    String employeeName;
    Designation designation;
    Date birthDate;
    String mobileNo;
    String email;
    ResidenceStatus residenceStatus;
    MaritalStatus maritalStatus;
    WorkDay workDay;
    Double workHours;
    String Location;
    String Note;
}

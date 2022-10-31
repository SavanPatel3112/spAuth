package com.example.authmoduls.employee.decorator;

import com.example.authmoduls.employee.enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeApplyLeaveResponse {
    String name;
    LeaveType leaveType;
    String fromDate;
    String toDate;
    String amount;
}

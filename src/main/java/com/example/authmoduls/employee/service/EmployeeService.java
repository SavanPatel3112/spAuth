package com.example.authmoduls.employee.service;

import com.example.authmoduls.common.decorator.EmployeeFilter;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.enums.EmployeeSortBy;
import com.example.authmoduls.employee.decorator.*;
import com.example.authmoduls.employee.enums.*;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

public interface EmployeeService {
    EmployeeResponse addEmployee(EmployeeRequest employeeRequest, Designation designation, MaritalStatus maritalStatus, ResidenceStatus residenceStatus, WorkDay workDay) throws InvocationTargetException, IllegalAccessException;

    List<EmployeeResponse> getAllEmployee() throws InvocationTargetException, IllegalAccessException;

    EmployeeApplyLeaveResponse addEmployeeLeave(EmployeeApplyLeaveAddRequest employeeApplyLeaveAddRequest, LeaveType leaveType) throws InvocationTargetException, IllegalAccessException, ParseException;

    List<EmployeeApplyLeaveResponse> getAllEmployeeLeave() throws InvocationTargetException, IllegalAccessException;

    List<EmployeeResponse> getEmployeeByPagination(EmployeeFilter filter, FilterSortRequest.SortRequest<EmployeeSortBy> sort, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException;

    EmplDebtResponse addEmployeeDept(EmplAddDebtRequest emplAddDebtRequest, GivenFrom givenFrom);
}

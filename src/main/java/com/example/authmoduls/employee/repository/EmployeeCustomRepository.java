package com.example.authmoduls.employee.repository;

import com.example.authmoduls.common.decorator.EmployeeFilter;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.enums.EmployeeSortBy;
import com.example.authmoduls.employee.decorator.EmployeeResponse;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface EmployeeCustomRepository {
    List<EmployeeResponse> findAllEmployeeByFilterAndSortAndPage(EmployeeFilter filter, FilterSortRequest.SortRequest
            <EmployeeSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException;
}

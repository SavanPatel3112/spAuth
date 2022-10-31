package com.example.authmoduls.employee.repository;

import com.example.authmoduls.employee.model.EmployeeDebt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeDebtRepository extends MongoRepository<EmployeeDebt,String> {
}

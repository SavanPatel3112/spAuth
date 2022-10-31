package com.example.authmoduls.employee.repository;

import com.example.authmoduls.employee.model.EmployeeApplyLeave;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeApplyLeaveRepository extends MongoRepository<EmployeeApplyLeave,String> {
    List<EmployeeApplyLeave> findAllBySoftDeleteFalse();
}

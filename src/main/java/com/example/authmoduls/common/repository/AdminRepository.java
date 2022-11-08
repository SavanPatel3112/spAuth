package com.example.authmoduls.common.repository;

import com.example.authmoduls.common.model.AdminConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdminRepository extends MongoRepository<AdminConfiguration,String> {

}

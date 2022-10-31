package com.example.authmoduls.common.repository;

import com.example.authmoduls.common.model.AdminConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<AdminConfiguration,String> {
}

package com.example.authmoduls.common.repository;

import com.example.authmoduls.common.model.UserImportedData;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface ImportedDataRepository extends MongoRepository<UserImportedData,String> {


}
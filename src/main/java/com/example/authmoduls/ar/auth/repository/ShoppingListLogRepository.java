package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingListLogRepository extends MongoRepository<ShoppingListLog,String> {



}

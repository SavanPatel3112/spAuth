package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.ShoppingListLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListLogRepository extends MongoRepository<ShoppingListLog,String> {

    List<ShoppingListLog> findByLoginIdAndSoftDeleteIsFalse(String loginId);
}

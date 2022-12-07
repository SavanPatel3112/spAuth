package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.model.Login;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LoginRepository extends MongoRepository<Login,String>,LoginCustomRepository{
    Optional<Login> findByIdAndSoftDeleteIsFalse(String id);
    Optional<Login> findByEmailAndSoftDeleteIsFalse(String id);
    boolean existsByEmailAndSoftDeleteFalse(String email);

    List<Login> findAllBySoftDeleteFalse();

    boolean existsByIdAndSoftDeleteFalse(String id);
}

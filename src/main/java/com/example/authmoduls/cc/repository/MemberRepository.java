package com.example.authmoduls.cc.repository;

import com.example.authmoduls.cc.modal.MemberModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<MemberModel, String>, MemberCustomRepository {
    boolean existsByEmailAndSoftDeleteFalse(String email);

    Optional<MemberModel> findByIdAndSoftDeleteIsFalse(String id);

    List<MemberModel> findAllBySoftDeleteFalse();


}

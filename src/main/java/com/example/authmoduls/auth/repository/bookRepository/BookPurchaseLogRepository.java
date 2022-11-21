package com.example.authmoduls.auth.repository.bookRepository;

import com.example.authmoduls.auth.decorator.bookDecorator.BookPurchaseLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookPurchaseLogRepository extends MongoRepository<BookPurchaseLog, String> {

    Optional<BookPurchaseLog> findFirstByBookIdAndUserIdAndSoftDeleteIsFalse(String bookId, String userId);


}

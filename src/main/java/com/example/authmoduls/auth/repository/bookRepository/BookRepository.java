package com.example.authmoduls.auth.repository.bookRepository;

import com.example.authmoduls.auth.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book,String>, BookCustomRepository{
    Optional<Book> findByIdAndSoftDeleteIsFalse(String id);
    Optional<Book> findByBookNameAndSoftDeleteIsFalse(String bookName);


}

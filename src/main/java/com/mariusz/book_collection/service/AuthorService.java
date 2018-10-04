package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Optional<Author> findAuthorById(Long authorId);
    List<Author> findAll();
    List<Author> findByLastName(String lastName);

}

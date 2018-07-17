package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findBookById(Long id);
    Optional<Book> findBookByIsbn(String isbn);

    List<Book> findAllBooks();
}

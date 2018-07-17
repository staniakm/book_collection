package com.mariusz.book_collection.repository;

import com.mariusz.book_collection.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long id);
    Optional<Book> findByIsbn(String isbn);
    Book save(Book book);
}

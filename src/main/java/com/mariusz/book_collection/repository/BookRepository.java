package com.mariusz.book_collection.repository;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);
    List<Book> findAllByAuthor_AuthorId(Long authorId);

    List<Book> findAllByTitleIgnoreCaseContains(String title);
    List<Book> findAllByAuthorIn(List<Author> authors);
}

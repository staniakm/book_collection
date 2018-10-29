package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.BookForm;
import com.mariusz.book_collection.entity.Shelf;
import jdk.internal.org.objectweb.asm.commons.Remapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {

    Optional<Book> findBookById(Long id);
    Optional<Book> findBookByIsbn(String isbn);
    List<Book> findAllBooks();
    Book saveOrUpdate(Book book);
    Book saveOrUpdate(Long bookId, BookForm bookForm);
    Book putBookOnShelf(Book book, Shelf shef);

    List<Book> findBooksByAuthorId(Long authorId);

    List<Book> findBookByAuthors(List<Author> authors);

    List<Book> findBookByTitle(String title);

    void saveOrUpdate(Map<String,Object> updates, Long id);

    Book addNewBook(BookForm bookForm);

    Optional<BookForm> findBookFormById(Long bookId);
}

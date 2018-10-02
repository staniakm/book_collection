package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        String pattern = "([^\\d])+";
        String verifiedIsbn = isbn.replaceAll(pattern,"");

        return bookRepository.findByIsbn(verifiedIsbn);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveOrUpdate(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book putBookOnShelf(Book book, Shelf shelf) {
        book.setShelf(shelf);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findBooksByAuthorId(Long authorId) {
        return bookRepository.findAllByAuthor_AuthorId(authorId);
    }

    @Override
    public List<Book> findbookbyAuthor(Optional<Author> author) {
        return author.map(bookRepository::findAllByAuthor).orElse(Collections.emptyList());
    }
}

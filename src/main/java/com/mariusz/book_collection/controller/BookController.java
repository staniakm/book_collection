package com.mariusz.book_collection.controller;

import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/books")
public class BookController {


    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "", produces = {"application/hal+json"})
    public ResponseEntity<Book> create(@RequestBody final Book book) {
        Book createdBook = bookService.saveOrUpdate(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<Book> update(@PathVariable("id") Long bookId, @RequestBody Book book){

        return bookService.findBookById(bookId).map(foundBook -> {
            book.setId(bookId);
            return new ResponseEntity<>(bookService.saveOrUpdate(book),HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

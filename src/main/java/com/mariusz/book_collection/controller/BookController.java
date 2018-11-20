package com.mariusz.book_collection.controller;

import com.mariusz.book_collection.entity.BookForm;
import com.mariusz.book_collection.service.AuthorService;
import com.mariusz.book_collection.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping(value = "/{id}")
    public String getBook(@PathVariable("id") Long bookId, Model model){
        bookService
                .findBookById(bookId)
                .map(foundBook ->  model.addAttribute("book", foundBook));
        return "book_details";
    }

    @GetMapping(value = "/{id}/edit")
    public String editBook(@PathVariable("id") Long bookId, Model model){

        bookService
                .findBookFormById(bookId)
                .map(foundBook ->  model.addAttribute("bookForm", foundBook));
        return "book_details_edit";
    }

    @PutMapping("/{id}/save")
    public String addNewBook(@PathVariable("id") Long id, @ModelAttribute("bookForm") BookForm bookForm) {
        bookService.saveOrUpdate(id, bookForm);
        return "redirect:/books/bookList";
    }

    @GetMapping(value = "/bookList")
    public String getAllBooks(Model model){
        model.addAttribute("books", bookService.findAllBooks());
        return "book_list";
    }

    @GetMapping("/addBook")
    public String addNewBook (Model model){
        BookForm bookForm = new BookForm();
        model.addAttribute("bookForm", bookForm);
        return "book_new";
    }

    @PostMapping("/addBook")
    public String addNewBook(@ModelAttribute("bookForm") BookForm bookForm) {
        bookService.addNewBook(bookForm);
        return "redirect:/books/bookList";
    }


}

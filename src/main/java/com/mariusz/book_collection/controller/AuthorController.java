package com.mariusz.book_collection.controller;

import com.mariusz.book_collection.entity.AuthorForm;
import com.mariusz.book_collection.entity.BookForm;
import com.mariusz.book_collection.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/authors")
public class AuthorController {


    @Autowired
    private final AuthorService authorService;


    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "authorList")
    public String getAuthors(Model model){
        model.addAttribute("authors", authorService.findAll());
        return "author_list";
    }

    @GetMapping(value = "/{id}")
    public String getAuthorDetails(@PathVariable("id") Long authorId, Model model){
        authorService
                .findAuthorById(authorId)
                .map(foundAuthor ->  model.addAttribute("author", foundAuthor));
        return "author_details";
    }

    @GetMapping("/addAuthor")
    public String addNewAuthor (Model model){
        AuthorForm authorForm = new AuthorForm();
        model.addAttribute("authorForm", authorForm);
        return "author_new";
    }

    @PostMapping("/addAuthor")
    public String addNewAuthor(@ModelAttribute("authorForm") AuthorForm authorForm) {
        authorService.addNewAuthor(authorForm);
        return "redirect:/authors/authorList";
    }
}

package com.mariusz.book_collection.controller;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.service.AuthorService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @GetMapping(value = "/{authorId}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("authorId") Long authorId){
        return authorService
                .findAuthorById(authorId)
                .map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Author>> getAuthors(){
        final val all = authorService.findAll();
      return all.isEmpty()?new ResponseEntity<>(HttpStatus.NOT_FOUND): new ResponseEntity<>(all, HttpStatus.OK);
    }




}

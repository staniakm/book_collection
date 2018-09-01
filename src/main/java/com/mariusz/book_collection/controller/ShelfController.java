package com.mariusz.book_collection.controller;

import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.service.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/shelfs")
public class ShelfController {

    private final ShelfService shelfService;

    @Autowired
    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @GetMapping("")
    public ResponseEntity<List<Shelf>> getAllShelfs(){
        return new ResponseEntity<>(shelfService.getAllShelfs(), HttpStatus.OK);
    }


}

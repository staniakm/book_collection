package com.mariusz.book_collection.controllerRest;

import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.service.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/shelfs")
public class ShelfRestController {

    private final ShelfService shelfService;

    @Autowired
    public ShelfRestController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @GetMapping("")
    public ResponseEntity<List<Shelf>> getAllShelfs() {
        List<Shelf> shelfs = shelfService.getAllShelfs();
        return shelfs.size() > 0
                ? new ResponseEntity<>(shelfs, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Shelf> getShelf(@PathVariable("id") Long shelfId) {
        return shelfService
                .findShelfById(shelfId)
                .map(shelf -> new ResponseEntity<>(shelf, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

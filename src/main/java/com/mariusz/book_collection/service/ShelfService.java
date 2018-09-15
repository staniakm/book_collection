package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Shelf;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ShelfService {

    List<Shelf> getAllShelfs();
    Optional<Shelf> findShelfById(Long shelfId);

}

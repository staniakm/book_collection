package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Shelf;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShelfService {

    List<Shelf> getAllShelfs();

}

package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.repository.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;

    @Autowired
    public ShelfServiceImpl(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    @Override
    public List<Shelf> getAllShelfs() {
        return shelfRepository.findAll();
    }

    @Override
    public Optional<Shelf> findShelfById(Long shelfId) {
        return Optional.empty();
    }
}

package com.mariusz.book_collection.repository;

import com.mariusz.book_collection.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    Optional<Shelf> findByDescription(String description);
    Optional<Shelf> findById(Long shelfId);

}

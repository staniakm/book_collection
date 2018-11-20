package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.AuthorForm;
import com.mariusz.book_collection.mapper.AuthorFormMapper;
import com.mariusz.book_collection.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorFormMapper authorFormMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorFormMapper authorFormMapper) {
        this.authorRepository = authorRepository;
        this.authorFormMapper = authorFormMapper;
    }

    @Override
    public Optional<Author> findAuthorById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        return authorRepository.findAllByLastName(lastName);
    }

    @Override
    public Author addNewAuthor(AuthorForm authorForm) {
        Author author = authorFormMapper.mapToAuthor(authorForm);
        return authorRepository.save(author);
    }
}

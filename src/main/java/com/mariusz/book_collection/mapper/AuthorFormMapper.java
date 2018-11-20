package com.mariusz.book_collection.mapper;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.AuthorForm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorFormMapper {

    public Author mapToAuthor(AuthorForm authorForm){
        Author author = new Author();
        author.setFirstName(authorForm.getFirstName());
        author.setLastName(authorForm.getLastName());
        return author;
    }

    public Author updateAthor(Author author, AuthorForm authorForm){
        author.setFirstName(authorForm.getFirstName());
        author.setLastName(authorForm.getLastName());
        return author;
    }

    public Optional<AuthorForm> mapToForm(Optional<Author> author) {

        if (author.isPresent()){
            AuthorForm form = new AuthorForm();
            return author.map(existingAuthor ->{
                form.setId(existingAuthor.getAuthorId());
                form.setFirstName(existingAuthor.getFirstName());
                form.setLastName(existingAuthor.getLastName());
                return form;
            });
        }else {
            return Optional.empty();
        }
    }
}

package com.mariusz.book_collection.repository;

import com.mariusz.book_collection.entity.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void existingAuthorShouldBeReturned(){
        Author author = new Author();
        author.setFirstName("Andrzej");
        author.setLastName("Sapkowski");

        Author authorSaved = testEntityManager.persist(author);

        Optional<Author> find = authorRepository.findById(authorSaved.getAuthorId());
        assertThat(find.isPresent()).isTrue();
        assertThat(find.get().getFirstName()).isEqualTo("Andrzej");
        testEntityManager.flush();
    }
}

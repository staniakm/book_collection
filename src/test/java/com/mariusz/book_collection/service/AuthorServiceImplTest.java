package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.repository.AuthorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @Before
    public void setUp(){
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    public void getAuthorById_shouldReturnOptionalAuthor(){
        Author author = new Author();
        author.setLastName("Sapkowski");
        author.setFirstName("Andrzej");
        author.setAuthorId(1L);

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        Optional<Author> newAuthor = authorService.findAuthorById(1L);

        assertThat(newAuthor.isPresent()).isTrue();
        assertThat( newAuthor.get().getFirstName()).isEqualTo(author.getFirstName());
        assertThat(newAuthor.get().getAuthorId()).isEqualTo(author.getAuthorId());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    public void getAuthorById_shouldReturnOptionalEmpty(){
        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        Optional<Author> newAuthor = authorService.findAuthorById(1L);

        assertThat(newAuthor.isPresent()).isFalse();
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    public void getAllAuthors_shouldReturnList(){
        Author author = new Author();
        author.setAuthorId(1L);
        Author author2 = new Author();
        author2.setAuthorId(2L);

        given(authorRepository.findAll()).willReturn(Arrays.asList(author, author2));

        List<Author> authors = authorService.findAll();

        assertThat(authors.size()).isEqualTo(2);
        assertThat(authors.contains(author)).isTrue();
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void getAllAuthors_shouldReturnEmptyList(){
        given(authorRepository.findAll()).willReturn(Collections.emptyList());

        List<Author> authors = authorService.findAll();

        assertThat(authors.isEmpty()).isTrue();
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void getAuthorByLastName_shouldReturnOptionalAuthor(){
        Author author = new Author();
        author.setLastName("Sapkowski");
        author.setFirstName("Andrzej");
        author.setAuthorId(2L);

        given(authorRepository.findAllByLastName(anyString())).willReturn(Collections.singletonList(author));

        List<Author> newAuthor = authorService.findByLastName(author.getLastName());

        assertThat(newAuthor.contains(author)).isTrue();
        assertThat(newAuthor.get(0).getFirstName()).isEqualTo(author.getFirstName());
        assertThat(newAuthor.get(0).getAuthorId()).isEqualTo(author.getAuthorId());
        verify(authorRepository, times(1)).findAllByLastName(anyString());
    }

    @Test
    public void getAuthorByLastName_shouldReturnOptionalEmpty(){
        given(authorRepository.findAllByLastName(anyString())).willReturn(Collections.emptyList());

        List<Author> newAuthor = authorService.findByLastName("author name");

        assertThat(newAuthor.isEmpty()).isTrue();
        verify(authorRepository, times(1)).findAllByLastName(anyString());
    }



}
package com.mariusz.book_collection.service;


import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class BookServiceImplTest {
    @TestConfiguration
    static class BookServiceImplConfiguration {
        private BookRepository bookRepository = mock(BookRepository.class);

        @Bean
        BookService bookService() {
            return new BookServiceImpl(bookRepository);
        }

        @Bean
        BookRepository bookRepository() {
            return bookRepository;
        }
    }

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;



    @Before
    public void setup() {
        Book book = new Book();
        book.setAuthor("Lee Carroll");
        book.setId(1L);
        book.setTitle("Królestwo czerwonego łabędzia");
        book.setDescription("Ludzie widzą tylko to co chcą widzieć. Zajrzyj głębiej...");
        book.setIsbn("9788376489117");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        when(bookRepository.findByIsbn("9788376489117")).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbn("0000000000")).thenReturn(Optional.empty());

    }

    @Test
    public void shouldGetDataFromRepositoryWithOnlyOneRequest() {
        bookService.findBookById(1L);
        verify(bookRepository, times(1)).findById(1L);
        reset(bookRepository);
    }

    @Test
    public void shouldReturnBookWhenProvideCorrectIsbn() {
        Optional<Book> result = bookService.findBookByIsbn("9788376489117");

        assertThat(result.isPresent()).isTrue();

        Book book = null;
        if (result.isPresent()) {
            book = result.get();
            assertThat(book.getAuthor()).isEqualTo("Lee Carroll");
            assertThat(book.getId()).isEqualTo(1L);
            assertThat(book.getTitle()).isEqualTo("Królestwo czerwonego łabędzia");
            assertThat(book.getDescription()).isEqualTo("Ludzie widzą tylko to co chcą widzieć. Zajrzyj głębiej...");
            assertThat(book.getIsbn()).isEqualTo("9788376489117");
        }

        verify(bookRepository, times(1)).findByIsbn("9788376489117");
        reset(bookRepository);
    }


    @Test
    public void shouldReturnEnptyOptionalWhenProvideIncorrectIsbn() {
        Optional<Book> result = bookService.findBookByIsbn("0000000000");

        assertThat(result.isPresent()).isFalse();

        verify(bookRepository, times(1)).findByIsbn("0000000000");
        reset(bookRepository);
    }

    @Test
    public void shouldReturnBookWhenProvideCorrectId() {
        Optional<Book> result = bookService.findBookById(1L);

        assertThat(result.isPresent()).isTrue();

        Book book = null;
        if (result.isPresent())
            book = result.get();

        assertThat(book != null).isTrue();
        verify(bookRepository, times(1)).findById(1L);
        reset(bookRepository);
    }

    @Test
    public void shouldReturnEmptyOptionalIfBookDoesNotExists(){
        Optional<Book> result = bookService.findBookById(2L);

        assertThat(result.isPresent()).isFalse();

        verify(bookRepository, times(1)).findById(2L);
        reset(bookRepository);
    }




}
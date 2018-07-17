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

import java.util.Arrays;
import java.util.List;
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

        Book book2 = new Book();
        book2.setAuthor("Test Author");
        book2.setId(2L);
        book2.setTitle("Test title");
        book2.setDescription("Desctiption that will be tested...");
        book2.setIsbn("1234567899");

        Book newBookWithoutId = new Book();
        newBookWithoutId.setIsbn("1236547899");
        newBookWithoutId.setDescription("No id book");
        newBookWithoutId.setTitle("Book without ID");
        newBookWithoutId.setAuthor("Test");

        Book newBookWithId = new Book();
        newBookWithId.setAuthor(newBookWithoutId.getAuthor());
        newBookWithId.setDescription(newBookWithoutId.getDescription());
        newBookWithId.setTitle(newBookWithoutId.getTitle());
        newBookWithId.setIsbn(newBookWithoutId.getIsbn());
        newBookWithId.setId(3L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        when(bookRepository.findByIsbn("9788376489117")).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbn("0000000000")).thenReturn(Optional.empty());
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book,book2));
        when(bookRepository.save(newBookWithoutId)).thenReturn(newBookWithId);

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

        Book book ;
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
    public void shouldReturnBookWhenProvideCorrectIsbnWithSeparatedDigits() {
        Optional<Book> result = bookService.findBookByIsbn("978-837-648-911-7");

        assertThat(result.isPresent()).isTrue();

        Book book ;
        if (result.isPresent()) {
            book = result.get();
            assertThat(book.getAuthor()).isEqualTo("Lee Carroll");
            assertThat(book.getId()).isEqualTo(1L);
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

    @Test
    public void shouldReturnListOfTwoElementsWhenGetAllMethodCalled(){
        List<Book> books = bookService.findAllBooks();

        assertThat(books.size()).isEqualTo(2);
        assertThat(books.stream().anyMatch(book -> book.getId()==1L)).isTrue();
        assertThat(books.stream().anyMatch(book -> book.getId()==2L)).isTrue();

        verify(bookRepository,times(1)).findAll();
        reset(bookRepository);
    }

    @Test
    public void shouldReturnBookWithIdWhenSaveBook(){
        Book book = new Book();
        book.setIsbn("1236547899");
        book.setDescription("No id book");
        book.setTitle("Book without ID");
        book.setAuthor("Test");
        Book saved = bookService.save(book);

        assertThat(book.getId()).isEqualTo(null);
        assertThat(saved!=null).isTrue();
        assertThat(saved.getId()!=null).isTrue();

        assertThat(book.getTitle()).isEqualTo(saved.getTitle());
        assertThat(book.getAuthor()).isEqualTo(saved.getAuthor());
        assertThat(book.getDescription()).isEqualTo(saved.getDescription());
        assertThat(book.getIsbn()).isEqualTo(saved.getIsbn());
    }



}
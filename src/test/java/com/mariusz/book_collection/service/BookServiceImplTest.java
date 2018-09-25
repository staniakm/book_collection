package com.mariusz.book_collection.service;


import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    @Before
    public void setup() {
        bookService = new BookServiceImpl(bookRepository);

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

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(bookRepository.save(newBookWithoutId)).willReturn(newBookWithId);

    }

    @Test
    public void shouldGetDataFromRepositoryWithOnlyOneRequest() {
        bookService.findBookById(1L);
        verify(bookRepository, times(1)).findById(1L);
        reset(bookRepository);
    }

    @Test
        public void shouldReturnBookWhenProvideCorrectIsbn() {
        Book book1 = new Book();
        book1.setAuthor("Lee Carroll");
        book1.setId(1L);
        book1.setTitle("Królestwo czerwonego łabędzia");
        book1.setDescription("Ludzie widzą tylko to co chcą widzieć. Zajrzyj głębiej...");
        book1.setIsbn("9788376489117");

        given(bookRepository.findByIsbn("9788376489117")).willReturn(Optional.of(book1));
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
        Book book1 = new Book();
        book1.setAuthor("Lee Carroll");
        book1.setId(1L);
        book1.setTitle("Królestwo czerwonego łabędzia");
        book1.setDescription("Ludzie widzą tylko to co chcą widzieć. Zajrzyj głębiej...");
        book1.setIsbn("9788376489117");

        given(bookRepository.findByIsbn("9788376489117")).willReturn(Optional.of(book1));

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
        Book book1 = new Book();
        book1.setAuthor("Lee Carroll");
        book1.setId(1L);
        book1.setTitle("Królestwo czerwonego łabędzia");
        book1.setDescription("Ludzie widzą tylko to co chcą widzieć. Zajrzyj głębiej...");
        book1.setIsbn("9788376489117");

        Book book2 = new Book();
        book2.setAuthor("Test Author");
        book2.setId(2L);
        book2.setTitle("Test title");
        book2.setDescription("Desctiption that will be tested...");
        book2.setIsbn("1234567899");

        given(bookRepository.findAll()).willReturn(Arrays.asList(book1, book2));

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
        Book saved = bookService.saveOrUpdate(book);

        assertThat(book.getId()).isEqualTo(null);
        assertThat(saved!=null).isTrue();
        assertThat(saved.getId()!=null).isTrue();

        assertThat(book.getTitle()).isEqualTo(saved.getTitle());
        assertThat(book.getAuthor()).isEqualTo(saved.getAuthor());
        assertThat(book.getDescription()).isEqualTo(saved.getDescription());
        assertThat(book.getIsbn()).isEqualTo(saved.getIsbn());
    }

    @Test
    public void updateBookShouldReturnBookWithNewData(){
        //given
        Optional<Book> result = bookService.findBookById(1L);
        Book book = result.orElse(new Book());
        book.setTitle("New title for book 1");

        //when
        when(bookRepository.save(book)).thenReturn(book);
        Book newBook = bookService.saveOrUpdate(book);

        //then
        assertThat(newBook!=null).isTrue();

        assertThat(book.getTitle()).isEqualTo("New title for book 1");
        assertThat(newBook.getTitle()).isEqualTo("New title for book 1");

        assertThat(newBook.getId()).isEqualTo(book.getId());

        verify(bookRepository,times(1)).save(book);
    }


    @Test
    public void putBookOnShelfReturnUpdatedBook(){

        Shelf shelf = new Shelf(1L,"Bedroom");
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Pinokio");
        book.setShelf(shelf);

        Book bookWithoutShelf = new Book();
        bookWithoutShelf.setId(1L);

        given(bookRepository.save(any(Book.class))).willReturn(book);

        Book bookOnShelf = bookService.putBookOnShelf(bookWithoutShelf, shelf);

        assertThat(bookOnShelf.getTitle()).isEqualToIgnoringCase("Pinokio");
        assertThat(bookOnShelf.getShelf().getDescription()).isEqualToIgnoringCase("Bedroom");

    }


}
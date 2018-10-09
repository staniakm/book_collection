package com.mariusz.book_collection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.service.AuthorService;
import com.mariusz.book_collection.service.BookService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookController bookController;

    private JacksonTester<Book>jacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void typedIdShouldReturnSpecifiedBook() throws Exception {

        //given
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Pinokio");

        given(bookService.findBookById(1L))
                .willReturn(Optional.of(book));

        //when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/books/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualToIgnoringCase(jacksonTester.write(book).getJson());
    }

    @Test
    public void typedIncorrectIdShouldReturnEmptyOptionalAndNotFoundStatus() throws Exception {

        //given
        given(bookService.findBookById(1L))
                .willReturn(Optional.empty());

        //when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/books/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void typedRequestShouldReturnAllBooks() throws Exception {

        //given
        Book book1 = new Book(1L,"Pinokio","312312423","Story about wooden boy.");
        Book book2 = new Book(2L,"Martian","322312423","Story about mars and a man.");

        //when
        when(bookService.findAllBooks()).thenReturn(new ArrayList<>(Arrays.asList(book1, book2)));

        //when
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[1].title", Matchers.is("Martian")))
                .andExpect(jsonPath("$[1].isbn", Matchers.is("322312423")));

        verify(bookService, times(1)).findAllBooks();
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void typedRequestShouldReturnEmptyListIfNoBooksExists() throws Exception {

        //given

        //when
        when(bookService.findAllBooks()).thenReturn(new ArrayList<>());

        //when
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findAllBooks();
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void typedRequestShouldCreateNewBook() throws Exception {

        //given

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Game of Throne");

        given(bookService.saveOrUpdate(any(Book.class))).willReturn(book);

        //when

        MockHttpServletResponse response = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON).content(jacksonTester
                        .write(new Book()).getJson()))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jacksonTester.write(book).getJson());
    }

    @Test
    public void typedPutRequestShouldUpdateExistingBook() throws Exception {

        //given
        Book insertBook = new Book();
        insertBook.setId(1L);
        insertBook.setTitle("Game of Throne");

        Book returnBook = new Book();
        returnBook.setId(1L);
        returnBook.setTitle("Game of Throne - Fire and Ice");

        given(bookService.saveOrUpdate(insertBook)).willReturn(returnBook);

        //when
        when(bookService.findBookById(1L)).thenReturn(Optional.of(returnBook));

        MockHttpServletResponse response = mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON).content(jacksonTester
                        .write(insertBook).getJson()))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jacksonTester.write(returnBook).getJson());
    }

    @Test
    public void shouldReturnBookByValidIsbn() throws Exception {

        given(bookService.findBookByIsbn("123456789")).willReturn(Optional.of(new Book(1L, "Pinokio", "123456789", "")));

        mockMvc.perform(get("/api/books/book?isbn=123456789").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",is("Pinokio")))
                .andExpect(jsonPath("$.isbn",is("123456789")));
    }

    @Test
    public void shouldReturnBooksListByPartTitle() throws Exception {

        given(bookService.findBookByTitle(anyString())).willReturn(Collections.singletonList(new Book(1L, "Powrót zwiadowcy","","")));

        mockMvc.perform(get("/api/books/search?title=zwiad").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Powrót zwiadowcy")));
    }

    @Test
    public void shouldReturnEmptyList() throws Exception {

        given(bookService.findBookByTitle(anyString())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books/search?title=zwiad").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void shouldReturnBooksListByAuthorId() throws Exception {
        Book book = new Book(1L, "Powrót zwiadowcy","","");

        given(bookService.findBooksByAuthorId(1L)).willReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books/author?authorId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Powrót zwiadowcy")));
    }

    @Test
    public void shouldReturnBooksListByAuthorName() throws Exception {
        Book book = new Book(1L, "Powrót zwiadowcy","","");

        given(authorService.findByLastName(anyString())).willReturn(Collections.singletonList(new Author(1L, "Andrzej", "Sapkowski")));
        given(bookService.findBookByAuthors(anyList())).willReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books/author?name=sapkowski").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Powrót zwiadowcy")));
    }
}
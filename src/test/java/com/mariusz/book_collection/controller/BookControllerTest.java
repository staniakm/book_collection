package com.mariusz.book_collection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.service.BookService;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private JacksonTester<Book>jacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }


    @Test
    public void typedRequestShouldCreateNewBook() throws Exception {

        //given

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Game of Throne");
        book.setAuthor("Gorge RR Martin");

        given(bookService.saveOrUpdate(any(Book.class))).willReturn(book);

        //when
        when(bookService.findBookById(1L)).thenReturn(Optional.of(book));

        MockHttpServletResponse response = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON).content(jacksonTester
                        .write(new Book()).getJson()))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jacksonTester.write(book).getJson());
    }

}
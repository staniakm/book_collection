package com.mariusz.book_collection.controllerRest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.service.AuthorService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class AuthorRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorRestController authorRestController;

    private JacksonTester<Author> jacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(authorRestController).build();
    }

    @Test
    public void typedId_ShouldReturnSpecicAuthor() throws Exception {
        //given
        Author author = new Author(1L, "Andrzej","Sapkowski");
        given(authorService.findAuthorById(anyLong()))
                .willReturn(Optional.of(author));

        //when
        mockMvc
                .perform(get("/api/authors/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Andrzej")))
                .andExpect(jsonPath("$.lastName", is("Sapkowski")));
    }

    @Test
    public void typedId_ShouldReturnNotFoundStatus() throws Exception {
        //given
        given(authorService.findAuthorById(anyLong()))
                .willReturn(Optional.empty());
        //when
        mockMvc
                .perform(get("/api/authors/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        //then
        verify(authorService,times(1)).findAuthorById(anyLong());
    }


    @Test
    public void typedRequestShouldReturnAllAuthors() throws Exception {

        //given
        Author author1 = new Author(1L, "Andrzej","Sapkowski");
        Author author2 = new Author(2L, "Paolo","Coelio");
        given(authorService.findAll()).willReturn(Arrays.asList(author1,author2));

        //when
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].authorId", Matchers.is(1)))
                .andExpect(jsonPath("$[0].firstName", Matchers.is("Andrzej")))
                .andExpect(jsonPath("$[1].authorId", Matchers.is(2)))
                .andExpect(jsonPath("$[1].firstName", Matchers.is("Paolo")));

        verify(authorService, times(1)).findAll();
        verifyNoMoreInteractions(authorService);
    }


}

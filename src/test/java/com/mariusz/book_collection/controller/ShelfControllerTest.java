package com.mariusz.book_collection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.service.ShelfService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ShelfControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShelfService shelfService;

    @InjectMocks
    private ShelfController shelfController;

    private JacksonTester<Shelf> jacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(shelfController).build();
    }

    @Test
    public void typedRequestShouldReturnAllShelfs() throws Exception {

        //given
        Shelf shelf1 = new Shelf(1L,"Sypialnia 1");
        Shelf shelf2 = new Shelf(2L,"Sypialnia 2");

        //when
        when(shelfService.getAllShelfs()).thenReturn(new ArrayList<>(Arrays.asList(shelf1, shelf2)));

        //when
        mockMvc.perform(get("/api/shelfs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[1].description", Matchers.is(shelf2.getDescription())))
                ;

        verify(shelfService, times(1)).getAllShelfs();
        verifyNoMoreInteractions(shelfService);
    }

    @Test
    public void typedRequestShouldReturnEmptyList() throws Exception {

        //given

        //when
        when(shelfService.getAllShelfs()).thenReturn(new ArrayList<>());

        //when
        mockMvc.perform(get("/api/shelfs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)))
        ;

        verify(shelfService, times(1)).getAllShelfs();
        verifyNoMoreInteractions(shelfService);
    }

}
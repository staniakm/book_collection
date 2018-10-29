package com.mariusz.book_collection.controllerRest;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ShelfRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShelfService shelfService;

    @InjectMocks
    private ShelfRestController shelfRestController;

    private JacksonTester<Shelf> jacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(shelfRestController).build();
    }

    @Test
    public void typedIdShouldReturnSpecifiedShelf() throws Exception {

        //given
        Shelf shelf = new Shelf(1L,"Sypialnia");

        given(shelfService.findShelfById(1L))
                .willReturn(Optional.of(shelf));

        //when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/shelfs/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualToIgnoringCase(jacksonTester.write(shelf).getJson());
    }

    @Test
    public void typedIncorrectIdShouldReturnStatusNotFound() throws Exception {

        //given

        given(shelfService.findShelfById(1L))
                .willReturn(Optional.empty());

        //when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/shelfs/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(shelfService, times(1)).findShelfById(anyLong());
        verifyNoMoreInteractions(shelfService);
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
    public void typedRequestShouldReturnNotFoundIfListIsEmpty() throws Exception {

        when(shelfService.getAllShelfs()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/shelfs"))
                .andExpect(status().isNotFound());
        ;

        verify(shelfService, times(1)).getAllShelfs();
        verifyNoMoreInteractions(shelfService);
    }

}
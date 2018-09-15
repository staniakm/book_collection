package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.repository.BookRepository;
import com.mariusz.book_collection.repository.ShelfRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ShelfServiceImplTest {


    private ShelfService shelfService;
    @MockBean
    private ShelfRepository shelfRepository;



    @Before
    public void setup() {
        shelfService = new ShelfServiceImpl(shelfRepository);

        Shelf shelf1 = new Shelf(1L, "Sypialnia");
        Shelf shelf2 = new Shelf(2L, "Salon");


        when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelf1));
        when(shelfRepository.findById(3L)).thenReturn(Optional.empty());

        when(shelfRepository.findByDescription(shelf1.getDescription())).thenReturn(Optional.of(shelf1));
        when(shelfRepository.findByDescription("")).thenReturn(Optional.empty());
        when(shelfRepository.findAll()).thenReturn(Arrays.asList(shelf1, shelf2));

    }

    @Test
    public void shouldGetDataFromRepositoryWithOnlyOneRequest() {
        shelfService.findShelfById(1L);
        verify(shelfRepository, times(1)).findById(1L);
        reset(shelfRepository);
    }

    @Test
    public void shouldReturnShelfWhenProvideCorrectDescription() {
        Optional<Shelf> result = shelfService.findShelfByDescription("Sypialnia");
        assertThat(result.isPresent()).isTrue();

        Shelf shelf ;
        if (result.isPresent()) {
            shelf = result.get();
            assertThat(shelf.getId()).isEqualTo(1L);
            assertThat(shelf.getDescription()).isEqualTo("Sypialnia");
        }

        verify(shelfRepository, times(1)).findByDescription("Sypialnia");
        verifyNoMoreInteractions(shelfRepository);
        reset(shelfRepository);
    }

    @Test
    public void shouldEmptyOptionalShelfWhenProvideIncorrectDescription() {
        Optional<Shelf> result = shelfService.findShelfByDescription("");
        assertFalse(result.isPresent());

        verify(shelfRepository, times(1)).findByDescription("");
        verifyNoMoreInteractions(shelfRepository);
        reset(shelfRepository);
    }

    @Test
    public void shouldReturnShelfWhenProvideCorrectId() {
        Optional<Shelf> result = shelfService.findShelfById(1L);
        assertThat(result.isPresent()).isTrue();

        Shelf shelf ;
        if (result.isPresent()) {
            shelf = result.get();
            assertThat(shelf.getId()).isEqualTo(1L);
            assertThat(shelf.getDescription()).isEqualTo("Sypialnia");
        }

        verify(shelfRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(shelfRepository);
        reset(shelfRepository);
    }

    @Test
    public void shouldEmptyOptionalShelfWhenProvideIncorrectId() {
        Optional<Shelf> result = shelfService.findShelfById(3L);
        assertFalse(result.isPresent());

        verify(shelfRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(shelfRepository);
        reset(shelfRepository);
    }

    @Test
    public void shouldReturnListOfTwoElementsWhenGetAllMethodCalled(){
        List<Shelf> shelfs = shelfService.getAllShelfs();

        assertThat(shelfs.size()).isEqualTo(2);
        assertThat(shelfs.stream().anyMatch(book -> book.getId()==1L)).isTrue();
        assertThat(shelfs.stream().anyMatch(book -> book.getId()==2L)).isTrue();

        verify(shelfRepository,times(1)).findAll();
        reset(shelfRepository);
    }


}
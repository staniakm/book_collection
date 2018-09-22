package com.mariusz.book_collection.integration;

import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.repository.BookRepository;
import com.mariusz.book_collection.repository.ShelfRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ShelfRepository shelfRepository;

    @Before
    public void setUp() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Pinokio");
        book.setAuthor("Carl Collodi");
        bookRepository.save(book);

        Shelf shelf = new Shelf();
        shelf.setDescription("Bedroom");
        shelfRepository.save(shelf);

    }

    @After
    public void cleanUp(){
        bookRepository.flush();
        shelfRepository.flush();
    }


    @Test
    public void getBookById_willReturnBook() {

        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/1", Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase("Pinokio");
        assertThat(response.getBody().getAuthor()).isEqualToIgnoringCase("Carl Collodi");
    }

    @Test
    public void getBookById_willgetStatusNOtFound(){
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/22",Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getShelfById_willReturnShelf(){
        ResponseEntity<Shelf> response = restTemplate.getForEntity("/api/shelfs/1", Shelf.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDescription()).isEqualToIgnoringCase("Bedroom");
    }

    @Test
    public void getShelfById_willReturnNotFoundStatus(){
        ResponseEntity<Shelf> response = restTemplate.getForEntity("/api/shelfs/22", Shelf.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void putBook_willReturnBookWithId(){
        Book book = new Book();
        book.setTitle("Pankracy");

        Book newBook = restTemplate.postForObject("/api/books", book, Book.class);

        assertThat(newBook).isNotNull();
        assertThat(newBook.getId()).isNotZero();
        assertThat(newBook.getTitle()).isEqualToIgnoringCase("Pankracy");
    }

}

package com.mariusz.book_collection.integration;

import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.repository.BookRepository;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void setUp(){
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Pinokio");
        book.setAuthor("Carl Collodi");
        bookRepository.save(book);
    }


    @Test
    public void getBookById_willReturnBook(){

        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/1", Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase("Pinokio");
        assertThat(response.getBody().getAuthor()).isEqualToIgnoringCase("Carl Collodi");


    }
}

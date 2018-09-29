package com.mariusz.book_collection.integration;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.repository.AuthorRepository;
import com.mariusz.book_collection.repository.BookRepository;
import com.mariusz.book_collection.repository.ShelfRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ShelfRepository shelfRepository;

    @Autowired
    private AuthorRepository authorRepository;


    @Test
    public void getBookById_willReturnBook() {
        Book book = new Book();
        book.setTitle("Pinokio");
        book.setAuthor("Carl Collodi");
        bookRepository.save(book);
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/"+book.getId(), Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase("Pinokio");
        assertThat(response.getBody().getAuthor()).isEqualToIgnoringCase("Carl Collodi");
        bookRepository.deleteAll();
    }

    @Test
    public void getBookById_willgetStatusNotFound() {
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/221", Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test

    public void getBooks_willReturnAllBooks() {
        Book book = new Book();
        book.setTitle("Pinokio 2");
        book.setAuthor("Carl Collodi");
        bookRepository.save(book);

        Book book2 = new Book();
        book2.setTitle("80 days around the world.");
        book2.setAuthor("Jules Verne");
        bookRepository.save(book2);
        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().contains(book)).isTrue();
        assertThat(response.getBody().contains(book2)).isTrue();
        bookRepository.deleteAll();
    }


    @Test
    public void getBooks_willNotFoundIfThereAreNoBooks() {
        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getShelfById_willReturnShelf() {
        Shelf shelf = new Shelf();
        shelf.setDescription("Bedroom");
        shelfRepository.save(shelf);
        ResponseEntity<Shelf> response = restTemplate.getForEntity("/api/shelfs/1", Shelf.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDescription()).isEqualToIgnoringCase("Bedroom");
        shelfRepository.deleteAll();
    }

    @Test
    public void getShelfById_willReturnNotFoundStatus() {
        ResponseEntity<Shelf> response = restTemplate.getForEntity("/api/shelfs/22", Shelf.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void putBook_willReturnBookWithId() {
        Book book = new Book();
        book.setTitle("Pankracy");

        Book newBook = restTemplate.postForObject("/api/books", book, Book.class);

        assertThat(newBook).isNotNull();
        assertThat(newBook.getId()).isNotZero();
        assertThat(newBook.getTitle()).isEqualToIgnoringCase("Pankracy");
        bookRepository.deleteAll();
    }


    @Test
    public void putBook_willUpdateExistingBook() {
        //given
        Book insertBook = new Book();
        insertBook.setTitle("Game of Throne");
        insertBook.setAuthor("Gorge RR Martin");
        bookRepository.save(insertBook);

        Book returnBook = new Book();
        returnBook.setTitle("Game of Throne");
        returnBook.setAuthor("Gorge R.R. Martin");

        HttpHeaders httpHeaders = restTemplate
                .headForHeaders("/api/books/"+insertBook.getId());

        HttpEntity<Book> requestUpdate = new HttpEntity<>(returnBook, httpHeaders);
        ResponseEntity<Book> response = restTemplate.exchange(
                "/api/books/"+insertBook.getId(),
                HttpMethod.PUT,
                requestUpdate,
                Book.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAuthor()).isEqualToIgnoringCase(returnBook.getAuthor());
        bookRepository.deleteAll();
    }

    @Test
    public void getShelfs_willReturnAllShelfs() {
        Shelf shelf1 = new Shelf();
        shelf1.setDescription("Bedroom");

        Shelf shelf2 = new Shelf();
        shelf2.setDescription("Living room");
        shelfRepository.saveAll(Arrays.asList(shelf1, shelf2));

        ResponseEntity<List<Shelf>> response = restTemplate.exchange(
                "/api/shelfs/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Shelf>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().contains(shelf1)).isTrue();
        assertThat(response.getBody().contains(shelf2)).isTrue();
        shelfRepository.deleteAll();
    }


    @Test
    public void getBooks_willNotFoundIfThereAreNoShelfs() {
        ResponseEntity<List<Shelf>> response = restTemplate.exchange(
                "/api/shelfs/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Shelf>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void getBookByIsbn_willReturnBook() {
        Book book = new Book();
        book.setTitle("Pinokio");
        book.setAuthor("Carl Collodi");
        book.setIsbn("123-456-789-0");
        bookRepository.save(book);
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/book?isbn="+book.getIsbn(), Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase("Pinokio");
        assertThat(response.getBody().getAuthor()).isEqualToIgnoringCase("Carl Collodi");
        bookRepository.deleteAll();
    }

    @Test
    public void getBookByIsbn_willgetStatusNotFound() {
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/book?isbn=12-23-12", Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void getAuthorById_willReturnAuthor() {
        Author author = new Author(1L, "Andrzej","Sapkowski");
        authorRepository.save(author);
        ResponseEntity<Author> response = restTemplate
                .getForEntity("/api/authors/"+author.getAuthorId(), Author.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirstName()).isEqualToIgnoringCase(author.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualToIgnoringCase(author.getLastName());
        bookRepository.deleteAll();
    }

    @Test
    public void getAuthorById_willReturnNotFoundStatus() {
        ResponseEntity<Author> response = restTemplate
                .getForEntity("/api/authors/2", Author.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        bookRepository.deleteAll();
    }
}

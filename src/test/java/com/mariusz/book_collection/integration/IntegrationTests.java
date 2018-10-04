package com.mariusz.book_collection.integration;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.repository.AuthorRepository;
import com.mariusz.book_collection.repository.BookRepository;
import com.mariusz.book_collection.repository.ShelfRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ShelfRepository shelfRepository;

    @Autowired
    private AuthorRepository authorRepository;

@Before
public void setUp(){
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    shelfRepository.deleteAll();
}

    @Test
    public void getBookById_willReturnBook() {
        Book book = new Book();
        book.setTitle("Pinokio");
        bookRepository.save(book);
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/"+book.getId(), Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase("Pinokio");
        bookRepository.deleteAll();
    }

    @Test
    public void getBookById_willReturnBookWithAuthor() {
        Book book = new Book();
        Author author = new Author("Andrzej","Sapkowski");
        book.setTitle("Pinokio");
        book.setAuthor(author);
        authorRepository.save(author);
        bookRepository.save(book);
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/"+book.getId(), Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase("Pinokio");
        assertThat(response.getBody().getAuthor().getFirstName()).isEqualTo(author.getFirstName());
        bookRepository.deleteAll();
        authorRepository.deleteAll();
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
        bookRepository.save(book);

        Book book2 = new Book();
        book2.setTitle("80 days around the world.");
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
    public void getBooksByAuthor_willReturnAllBooksWithSpecifiedAuthor() {
        Book book = new Book();
        Author author = new Author("Jan","Kowalski");
        Author author2 = new Author("Paweł","Nowak");
        authorRepository.saveAll(Arrays.asList(author,author2));

        book.setTitle("Pinokio 2");
        book.setAuthor(author);
        bookRepository.save(book);

        Book book2 = new Book();
        book2.setTitle("Przygody Tomka");
        book2.setAuthor(author);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("80 days around the world.");
        book3.setAuthor(author2);
        bookRepository.save(book3);

        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/author?authorId="+author.getAuthorId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().contains(book)).isTrue();
        assertThat(response.getBody().contains(book2)).isTrue();
        assertThat(response.getBody().contains(book3)).isFalse();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void getBooksByAuthorName_willReturnAllBooksWithSpecifiedAuthor() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        Book book = new Book();
        Author author = new Author("Jan","Kowalski");
        Author author2 = new Author("Paweł","Nowak");
        authorRepository.saveAll(Arrays.asList(author,author2));

        book.setTitle("Pinokio 2");
        book.setAuthor(author);
        bookRepository.save(book);

        Book book2 = new Book();
        book2.setTitle("Przygody Tomka");
        book2.setAuthor(author);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("80 days around the world.");
        book3.setAuthor(author2);
        bookRepository.save(book3);

        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/author?name="+author.getLastName(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().contains(book)).isTrue();
        assertThat(response.getBody().contains(book2)).isTrue();
        assertThat(response.getBody().contains(book3)).isFalse();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void getBooksByAuthorName_willReturnAllBooksForAllFoundAuthors() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        Book book = new Book();
        Author author = new Author("Jan","Kowalski");
        Author author2 = new Author("Paweł","Kowalski");
        Author author3 = new Author("Paweł","Nowak");
        authorRepository.saveAll(Arrays.asList(author,author2));

        book.setTitle("Pinokio 2");
        book.setAuthor(author);
        bookRepository.save(book);

        Book book2 = new Book();
        book2.setTitle("Przygody Tomka");
        book2.setAuthor(author2);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("80 days around the world.");
        book3.setAuthor(author3);
        bookRepository.save(book3);

        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/author?name=Kowalski",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().contains(book)).isTrue();
        assertThat(response.getBody().contains(book2)).isTrue();
        assertThat(response.getBody().contains(book3)).isFalse();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void getBooksByPartOfTitle_willReturnAllBooksContainsSearchedSentence() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        Author author = new Author("Jan","Kowalski");
        authorRepository.saveAll(Collections.singletonList(author));


        Book book = new Book();
        book.setTitle("Pinikio.");
        book.setAuthor(author);
        bookRepository.save(book);

        Book book1 = new Book();
        book1.setTitle("Powrót Zwiadowcy.");
        book1.setAuthor(author);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Zwiad. Nowa nadzieja.");
        book2.setAuthor(author);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("Zwiadowcy. Wyprawa.");
        book3.setAuthor(author);
        bookRepository.save(book3);

        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/search?title=zwIaD",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(3);
        System.out.println(response.getBody().get(0).toString());

        assertThat(response.getBody().contains(book1)).isTrue();
        assertThat(response.getBody().contains(book2)).isTrue();
        assertThat(response.getBody().contains(book3)).isTrue();
        assertThat(response.getBody().contains(book)).isFalse();

    }

    @Test
    public void getBooksByAuthorWithoutParameters_willReturnEnmptyList() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/author",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(0);
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void getBooksByAuthor_willReturnEmptyListIfAuthodDontHaveBooks() {
        Author author = new Author("Jan","Kowalski");
        Author author2 = new Author("Paweł","Nowak");
        authorRepository.saveAll(Arrays.asList(author,author2));


        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/author?authorId="+author.getAuthorId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(0);
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void getBooksByAuthor_willReturnEmptyListIfAuthodNotExists() {

        ResponseEntity<List<Book>> response = restTemplate.exchange(
                "/api/books/author?authorId=555",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(0);
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }


    @Test
    public void getBooks_willNotFoundIfThereAreNoBooks() {
        bookRepository.deleteAll();
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
        bookRepository.save(insertBook);

        Book returnBook = new Book();
        returnBook.setTitle("Game of Throne - Fire and Ice");

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
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase(returnBook.getTitle());
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
        book.setIsbn("123-456-789-0");
        bookRepository.save(book);
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/book?isbn="+book.getIsbn(), Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualToIgnoringCase("Pinokio");
        bookRepository.deleteAll();
    }

    @Test
    public void getBookByIsbn_willgetStatusNotFound() {
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/book?isbn=12-23-12", Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void getAuthorById_willReturnAuthor() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        Author author = new Author();
        author.setFirstName("Andrzej");
        author.setLastName("Sapkowski");
        authorRepository.save(author);
        ResponseEntity<Author> response = restTemplate
                .getForEntity("/api/authors/"+author.getAuthorId(), Author.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirstName()).isEqualToIgnoringCase(author.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualToIgnoringCase(author.getLastName());
    }

    @Test
    public void getAuthorById_willReturnNotFoundStatus() {

        ResponseEntity<Author> response = restTemplate
                .getForEntity("/api/authors/2", Author.class);
        authorRepository.deleteAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAuthors_willReturnAllAuthors() {

        Author author1 = new Author( "Andrzej","Sapkowski");
        Author author2 = new Author( "Paolo","Coelio");
        authorRepository.saveAll(Arrays.asList(author1,author2));

        ResponseEntity<List<Author>> response = restTemplate
                .exchange("/api/authors/",HttpMethod.GET,null,
                    new ParameterizedTypeReference<List<Author>>() {});
        authorRepository.deleteAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().contains(author1)).isTrue();
        assertThat(response.getBody().contains(author2)).isTrue();

    }

    @Test
    public void getAllAuthors_willReturnNotFoundIfThereAreNoAuthors() {
        ResponseEntity<List<Author>> response = restTemplate
                .exchange("/api/authors/",HttpMethod.GET,null,
                        new ParameterizedTypeReference<List<Author>>() {});
        authorRepository.deleteAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}

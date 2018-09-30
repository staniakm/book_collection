package com.mariusz.book_collection.repository;

import com.mariusz.book_collection.entity.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void existingBookShouldBeReturned(){
        Book book = createBook();
        book.setTitle("Pinokio");
        Book testBookSavedInDb = testEntityManager.persist(book);

        Optional<Book> find = bookRepository.findById(testBookSavedInDb.getId());
        assertThat(find.isPresent()).isTrue();
        assertThat(find.get().getTitle()).isEqualTo("Pinokio");
        testEntityManager.flush();
    }

    @Test
    public void existingBookShouldBeReturnedAndContainAllData(){
        Book book = createBook();
        Book testBookSavedInDb = testEntityManager.persist(book);

        Book find = bookRepository.findById(testBookSavedInDb.getId()).orElse(new Book());

        assertThat(find.getId()).isNotZero();
        assertThat(find.getId()).isEqualTo(testBookSavedInDb.getId());
        assertThat(find.getTitle()).isEqualTo("Book without ID");
        testEntityManager.flush();
    }


    @Test
    public void returnEmptyOptionalIfBookDoesNotExistsById(){
        Book book = createBook();
        Book testBookSavedInDb = testEntityManager.persist(book);
        Optional<Book> find = bookRepository.findById(testBookSavedInDb.getId()+11);
        assertThat(find.isPresent()).isFalse();
        testEntityManager.flush();
    }

    @Test
    public void existingBookShouldBeReturnedByIsbn(){
        Book book = createBook();
        Book testBookSavedInDb = testEntityManager.persist(book);

        Optional<Book> find = bookRepository.findByIsbn(testBookSavedInDb.getIsbn());
        assertThat(find.isPresent()).isTrue();
        testEntityManager.flush();
    }

    @Test
    public void returnEmptyOptionalIfBookDoesNotExistsByIsbn(){
        Book book = createBook();
        Book testBookSavedInDb = testEntityManager.persist(book);
        Optional<Book> find = bookRepository.findByIsbn("0");
        assertThat(find.isPresent()).isFalse();
        testEntityManager.remove(testBookSavedInDb);
    }

    @Test
    public void saveBookShouldReturnBookWithId(){
        Book book = createBook();
        Book testBookSavedInDb = testEntityManager.persist(book);
        Optional<Book> savedInDb = bookRepository.findById(testBookSavedInDb.getId());
        Book savedBook = savedInDb.orElse(new Book());
        assertThat(savedBook).isEqualTo(testBookSavedInDb);
        testEntityManager.remove(book);
    }

    @Test
    public void saveBookShouldReturnBookWithIdAndChangedData(){
        final String BOOK_NEW_TITLE="Create new title to save it again";

        Book book = createBook();
        Book testBookSavedInDb = testEntityManager.persist(book);
        Optional<Book> savedInDb = bookRepository.findById(testBookSavedInDb.getId());
        Book savedBook = savedInDb.orElse(new Book());


        savedBook.setTitle(BOOK_NEW_TITLE);
        testEntityManager.persist(savedBook);

        Optional<Book> savedInDbNewTitle = bookRepository.findById(testBookSavedInDb.getId());
        Book bookWithNewTitle = savedInDbNewTitle.orElse(new Book());

        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getId()).isEqualTo(bookWithNewTitle.getId());
        assertThat(bookWithNewTitle.getTitle()).isEqualTo(BOOK_NEW_TITLE);
        testEntityManager.flush();
    }


    private Book createBook(){
        Book book = new Book();
        book.setIsbn("1236547899");
        book.setDescription("No id book");
        book.setTitle("Book without ID");
        return book;
    }
}
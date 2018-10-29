package com.mariusz.book_collection.service;

import com.mariusz.book_collection.entity.Author;
import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.BookForm;
import com.mariusz.book_collection.entity.Shelf;
import com.mariusz.book_collection.mapper.BookFormMapper;
import com.mariusz.book_collection.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookFormMapper bookFormMapper;
    private Book book;

    public BookServiceImpl(BookRepository bookRepository, BookFormMapper bookFormMapper) {
        this.bookRepository = bookRepository;
        this.bookFormMapper = bookFormMapper;
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        String pattern = "([^\\d])+";
        String verifiedIsbn = isbn.replaceAll(pattern,"");

        return bookRepository.findByIsbn(verifiedIsbn);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveOrUpdate(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book saveOrUpdate(Long bookId, BookForm bookForm) {
        Optional<Book> existingBook = bookRepository.findById(bookId);
        Book updatedBook = bookFormMapper.updateBook(existingBook.get(), bookForm);
        return bookRepository.save(updatedBook);
    }

    @Override
    public Book putBookOnShelf(Book book, Shelf shelf) {
        book.setShelf(shelf);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findBooksByAuthorId(Long authorId) {
        return bookRepository.findAllByAuthor_AuthorId(authorId);
    }

    @Override
    public List<Book> findBookByAuthors(List<Author> authors) {
        return bookRepository.findAllByAuthorIn(authors);
    }

    @Override
    public List<Book> findBookByTitle(String title) {
        return bookRepository.findAllByTitleIgnoreCaseContains(title);
    }

    @Override
    public void saveOrUpdate(Map<String, Object> updates, Long id) {
        Optional<Book> newBook = bookRepository.findById(id);

        if (newBook.isPresent()){
            book = newBook.get();
            for (Map.Entry<String, Object> value: updates.entrySet()
                 ) {
                try {
                    Field field = book.getClass().getDeclaredField(value.getKey());
                    field.setAccessible(true);
                    field.set(book, value.getValue());
                    field.setAccessible(false);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    //do nothing here
                }
            }
            bookRepository.save(book);
        }
    }

    @Override
    public Book addNewBook(BookForm bookForm) {
        Book book = bookFormMapper.mapToBook(bookForm);
        return bookRepository.save(book);
    }

    @Override
    public Optional<BookForm> findBookFormById(Long bookId) {
        return bookFormMapper.mapToForm(bookRepository.findById(bookId));
    }
}

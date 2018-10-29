package com.mariusz.book_collection.controllerRest;

import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.service.AuthorService;
import com.mariusz.book_collection.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/books")
public class RestBookController {


    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public RestBookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Book> getBook(@PathVariable("id") Long bookId){
        return bookService
                .findBookById(bookId)
                .map(foundBook -> new ResponseEntity<>(foundBook, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.findAllBooks();
        if (books.size()==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(value = "/author")
    public ResponseEntity<List<Book>> getAllBooksByAuthor(@RequestParam(value = "authorId", required = false) Long authorId,
                                                          @RequestParam(value = "name", required = false) String lastName){
        List<Book> books=Collections.emptyList();
        if (authorId!=null)
            books = bookService.findBooksByAuthorId(authorId);
        else if (lastName!=null){
            books = bookService.findBookByAuthors(authorService.findByLastName(lastName));
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam(value = "title") String title){
        List<Book> books = bookService.findBookByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    /***
     * Fing book by isbn
     * @param isbn - book isbn
     * @return optional book
     */
    @GetMapping(value = "/book")
    public ResponseEntity<Book> getBookByIsbn(@RequestParam(value = "isbn", required = false) String isbn){
       return bookService.findBookByIsbn(isbn).map(book ->
            new ResponseEntity<>(book, HttpStatus.OK)
        ).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "")
    public ResponseEntity<Book> create(@RequestBody final Book book) {
        Book createdBook = bookService.saveOrUpdate(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Book> update(@PathVariable("id") Long bookId, @RequestBody Book book){

        return bookService.findBookById(bookId).map(foundBook -> {
            book.setId(foundBook.getId());
            return new ResponseEntity<>(bookService.saveOrUpdate(book),HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> patch (@RequestBody Map<String, Object> updates, @PathVariable("id") Long id){
        Optional<Book> book = bookService.findBookById(id);
        if (book.isPresent()) {
            Book book1 = book.get();
            for (Field field : Book.class.getDeclaredFields()
                    ) {
                if (updates.containsKey(field.getName())) {
                    System.out.println(updates.get(field.getName()));
                    field.setAccessible(true);

                    try {
                        field.set(book1, updates.get(field.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        bookService.saveOrUpdate(updates, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

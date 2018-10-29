package com.mariusz.book_collection.mapper;

import com.mariusz.book_collection.entity.Book;
import com.mariusz.book_collection.entity.BookForm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookFormMapper {

    public Book mapToBook(BookForm bookForm){
        Book book = new Book();
        book.setTitle(bookForm.getTitle());
        book.setIsbn(bookForm.getIsbn());
        book.setDescription(bookForm.getDescription());
        return book;
    }

    public Book updateBook(Book book, BookForm bookForm){
        System.out.println(bookForm);
        book.setTitle(bookForm.getTitle());
        book.setIsbn(bookForm.getIsbn());
        book.setDescription(bookForm.getDescription());
        System.out.println(book);
        return book;
    }


    public Optional<BookForm> mapToForm(Optional<Book> book) {

        if (book.isPresent()){
            BookForm form = new BookForm();
            return book.map(existingBook ->{
                form.setId(existingBook.getId());
                form.setDescription(existingBook.getDescription());
                form.setTitle(existingBook.getTitle());
                form.setIsbn(existingBook.getIsbn());
                return form;
            });


        }else {
            return Optional.empty();
        }
    }
}

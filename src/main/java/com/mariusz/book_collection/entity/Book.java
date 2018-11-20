package com.mariusz.book_collection.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Data
@ToString
public class Book extends CommonFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @ManyToOne( optional = true)
    @JoinColumn(name = "author_id", nullable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Author author;

    private String isbn;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "shelf_id", nullable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    private Shelf shelf;

    public Book() {
    }

    public Book(Long id, @NotNull String title, String isbn, String description) {
        this.setId(id);
        this.title = title;
        setIsbn(isbn);
        this.description = description;
    }

    public void setIsbn(String isbn){
        String isbnPattern = "([^\\d])+";
        if (isbn!=null)
        this.isbn = isbn.replaceAll(isbnPattern,"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(this.getId(), book.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getId());
    }
}

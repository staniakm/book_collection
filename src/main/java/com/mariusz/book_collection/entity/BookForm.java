package com.mariusz.book_collection.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ToString
public class BookForm {

    private Long id;
    @NotNull
    private String title;
    private String isbn;
    private String description;

}

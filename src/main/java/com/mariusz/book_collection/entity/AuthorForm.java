package com.mariusz.book_collection.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@ToString
public class AuthorForm {
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}

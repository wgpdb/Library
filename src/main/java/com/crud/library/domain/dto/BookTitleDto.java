package com.crud.library.domain.dto;

import com.crud.library.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookTitleDto {

    private Long bookTitleId;
    private String bookTitle;
    private String bookAuthor;
    private int yearPublished;
    private List<Book> books;
}
package com.crud.library.domain.dto;

import com.crud.library.domain.BookStatus;
import com.crud.library.domain.BookTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDto {

    private Long bookId;
    private BookTitle bookTitle;
    private BookStatus bookStatus;
}
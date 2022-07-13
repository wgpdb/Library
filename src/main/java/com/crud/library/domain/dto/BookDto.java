package com.crud.library.domain.dto;

import com.crud.library.domain.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDto {

    private Long bookId;
    private Long bookTitleId;
    private BookStatus bookStatus;
}
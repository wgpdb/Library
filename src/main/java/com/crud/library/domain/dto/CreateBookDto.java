package com.crud.library.domain.dto;

import com.crud.library.domain.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBookDto {

    private Long bookId;
    private Long bookTitleId;
    private BookStatus bookStatus;
}
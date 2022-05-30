package com.crud.library.domain.dto;

import com.crud.library.domain.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBookStatusDto {

    private Long bookId;
    private BookStatus bookStatus;
}
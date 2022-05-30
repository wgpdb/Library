package com.crud.library.domain.dto;

import com.crud.library.domain.Book;
import com.crud.library.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@AllArgsConstructor
public class LibraryDto {

    private Long libraryId;
    private Book book;
    private Member member;
    private LocalDate issueDate;
    private LocalDate returnDate;
}
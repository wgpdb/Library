package com.crud.library.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LibraryBookIssueDto {

    private Long libraryId;
    private Long bookId;
    private Long memberId;
    private LocalDate issueDate;
    private LocalDate returnDate;
}
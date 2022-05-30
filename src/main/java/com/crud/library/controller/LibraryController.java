package com.crud.library.controller;

import com.crud.library.domain.Book;
import com.crud.library.domain.BookStatus;
import com.crud.library.domain.Library;
import com.crud.library.domain.Member;
import com.crud.library.domain.dto.LibraryBookIssueDto;
import com.crud.library.domain.dto.LibraryDto;
import com.crud.library.exception.*;
import com.crud.library.mapper.LibraryMapper;
import com.crud.library.service.BookDbService;
import com.crud.library.service.LibraryDbService;
import com.crud.library.service.MemberDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryDbService libraryDbService;
    private final BookDbService bookDbService;
    private final MemberDbService memberDbService;
    private final LibraryMapper libraryMapper;

    @GetMapping(value = "{bookIssueId}")
    public ResponseEntity<LibraryDto> getIssuedBook(@PathVariable Long bookIssueId)
            throws BookIssueNotFoundException {
        return ResponseEntity.ok(libraryMapper.mapToLibraryDto(libraryDbService.getIssuedBook(bookIssueId)));
    }

    @GetMapping
    public ResponseEntity<List<LibraryDto>> getAllIssuedBooks() {
        List<Library> issuedBooks = libraryDbService.getAllIssuedBooks();
        return ResponseEntity.ok(libraryMapper.mapToLibraryDtoList(issuedBooks));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "issue")
    public ResponseEntity<LibraryDto> issueBook(@RequestBody LibraryBookIssueDto libraryBookIssueDto)
            throws BookNotFoundException, MemberNotFoundException, BookNotAvailableException {
        if (bookDbService.getBookStatusById(libraryBookIssueDto.getBookId()).equals(BookStatus.AVAILABLE)) {
            Book book = bookDbService.getBook(libraryBookIssueDto.getBookId());
            book.setBookStatus(BookStatus.ISSUED);
            Member member = memberDbService.getMember(libraryBookIssueDto.getMemberId());
            Library bookIssue = libraryMapper.mapToLibraryBookIssue(libraryBookIssueDto, book, member);
            libraryDbService.saveBookIssue(bookIssue);
            return ResponseEntity.ok(libraryMapper.mapToLibraryDto(bookIssue));
        } else {
            throw new BookNotAvailableException();
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryDto> updateIssueBook(@RequestBody LibraryDto libraryDto) {
        Library bookReturn = libraryMapper.mapToLibrary(libraryDto);
        Library bookIssue = libraryDbService.saveBookIssue(bookReturn);
        return ResponseEntity.ok(libraryMapper.mapToLibraryDto(bookIssue));
    }

    @PutMapping(value = "return/{libraryId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long libraryId)
            throws BookIssueNotFoundException, BookNotIssuedException {
        if (libraryDbService.getIssuedBook(libraryId).getBook().getBookStatus().equals(BookStatus.ISSUED)) {
            Book book = libraryDbService.getIssuedBook(libraryId).getBook();
            book.setBookStatus(BookStatus.AVAILABLE);
            Library bookReturn = libraryDbService.getIssuedBook(libraryId);
            bookReturn.setReturnDate(LocalDate.now());
            libraryDbService.saveBookReturn(bookReturn);
            return ResponseEntity.ok().build();
        } else {
            throw new BookNotIssuedException();
        }
    }

    @DeleteMapping(value = "{bookIssueId}")
    public ResponseEntity<Void> deleteBookIssue(@PathVariable Long bookIssueId) {
        libraryDbService.deleteBookIssue(bookIssueId);
        return ResponseEntity.ok().build();
    }
}
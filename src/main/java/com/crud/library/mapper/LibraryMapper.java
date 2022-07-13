package com.crud.library.mapper;

import com.crud.library.domain.Book;
import com.crud.library.domain.Library;
import com.crud.library.domain.Member;
import com.crud.library.domain.dto.LibraryDto;
import com.crud.library.domain.dto.LibraryBookIssueDto;
import com.crud.library.exception.BookNotFoundException;
import com.crud.library.exception.MemberNotFoundException;
import com.crud.library.service.BookDbService;
import com.crud.library.service.MemberDbService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryMapper {

    private BookDbService bookDbService;
    private MemberDbService memberDbService;

    public Library mapToLibrary(final LibraryDto libraryDto) throws BookNotFoundException, MemberNotFoundException {
        return new Library(
                libraryDto.getLibraryId(),
                bookDbService.getBook(libraryDto.getBookId()),
                memberDbService.getMember(libraryDto.getMemberId()),
                libraryDto.getIssueDate(),
                libraryDto.getReturnDate()
        );
    }

    public Library mapToLibraryBookIssue(final LibraryBookIssueDto libraryBookIssueDto, final Book book,
                                         final Member member) {
        return new Library(
                libraryBookIssueDto.getLibraryId(),
                book,
                member,
                libraryBookIssueDto.getIssueDate(),
                libraryBookIssueDto.getReturnDate()
        );
    }

    public LibraryDto mapToLibraryDto(final Library library) {
        return new LibraryDto(
                library.getLibraryId(),
                library.getBook().getBookId(),
                library.getMember().getMemberId(),
                library.getIssueDate(),
                library.getReturnDate()
        );
    }

    public List<LibraryDto> mapToLibraryDtoList(final List<Library> libraryIssueList) {
        return libraryIssueList.stream()
                .map(this::mapToLibraryDto)
                .collect(Collectors.toList());
    }
}
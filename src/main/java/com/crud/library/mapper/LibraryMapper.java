package com.crud.library.mapper;

import com.crud.library.domain.Book;
import com.crud.library.domain.Library;
import com.crud.library.domain.Member;
import com.crud.library.domain.dto.LibraryDto;
import com.crud.library.domain.dto.LibraryBookIssueDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryMapper {

    public Library mapToLibrary(final LibraryDto libraryDto) {
        return new Library(
                libraryDto.getLibraryId(),
                libraryDto.getBook(),
                libraryDto.getMember(),
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
                library.getBook(),
                library.getMember(),
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
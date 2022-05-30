package com.crud.library.service;

import com.crud.library.domain.Library;
import com.crud.library.exception.BookIssueNotFoundException;
import com.crud.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryDbService {

    @Autowired
    private final LibraryRepository libraryRepository;

    public Library getIssuedBook(final Long bookIssueId) throws BookIssueNotFoundException {
        return libraryRepository.findById(bookIssueId).orElseThrow(BookIssueNotFoundException::new);
    }

    public List<Library> getAllIssuedBooks() {
        return libraryRepository.findAll();
    }

    public Library saveBookIssue(final Library library) {
        return libraryRepository.save(library);
    }

    public Library saveBookReturn(final Library library) {
        return libraryRepository.save(library);
    }

    public void deleteBookIssue(final Long bookIssueId) {
        if (libraryRepository.existsById(bookIssueId)) {
            libraryRepository.deleteById(bookIssueId);
        }
    }
}
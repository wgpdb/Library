package com.crud.library.service;

import com.crud.library.domain.BookTitle;
import com.crud.library.exception.BookTitleNotFoundException;
import com.crud.library.repository.BookTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookTitleDbService {

    @Autowired
    private final BookTitleRepository bookTitleRepository;

    public BookTitle getBookTitle(final Long bookTitleId) throws BookTitleNotFoundException {
        return bookTitleRepository.findById(bookTitleId).orElseThrow(BookTitleNotFoundException::new);
    }

    public List<BookTitle> getAllBookTitles() {
        return bookTitleRepository.findAll();
    }

    public BookTitle saveBookTitle(final BookTitle bookTitle) {
        return bookTitleRepository.save(bookTitle);
    }

    public void deleteBookTitle(final Long bookTitleId) {
        if (bookTitleRepository.existsById(bookTitleId)) {
            bookTitleRepository.deleteById(bookTitleId);
        }
    }
}
package com.crud.library.service;

import com.crud.library.domain.Book;
import com.crud.library.domain.BookStatus;
import com.crud.library.exception.BookNotFoundException;
import com.crud.library.exception.BookStatusDoesNotExistException;
import com.crud.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDbService {

    @Autowired
    private final BookRepository bookRepository;

    public Book getBook(final Long bookId) throws BookNotFoundException {
        return bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookStatus getBookStatusById(final Long bookId) throws BookNotFoundException {
        if (bookRepository.existsById(bookId)) {
            return bookRepository.findById(bookId).get().getBookStatus();
        } else {
            throw new BookNotFoundException();
        }
    }

    public List<Book> getBooksByStatus(final String status) throws BookStatusDoesNotExistException {
        try {
            BookStatus bookStatus = BookStatus.valueOf(status.toUpperCase());
            return bookRepository.findByBookStatus(bookStatus);
        } catch(Exception e) {
            throw new BookStatusDoesNotExistException();
        }
    }

    public long getBooksCountByStatus(final String status) throws BookStatusDoesNotExistException {
        try {
            BookStatus bookStatus = BookStatus.valueOf(status.toUpperCase());
            return bookRepository.countBooksByBookStatus(bookStatus);
        } catch(Exception e) {
            throw new BookStatusDoesNotExistException();
        }
    }

    public Book saveBook(final Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(final Book book) throws BookNotFoundException {
        if (bookRepository.existsById(book.getBookId())) {
            return bookRepository.save(book);
        } else {
            throw new BookNotFoundException();
        }
    }

    public void deleteBook(final Long bookId) {
        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
        }
    }
}
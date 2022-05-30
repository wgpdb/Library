package com.crud.library.controller;

import com.crud.library.domain.Book;
import com.crud.library.domain.BookTitle;
import com.crud.library.domain.dto.BookDto;
import com.crud.library.domain.dto.CreateBookDto;
import com.crud.library.domain.dto.UpdateBookStatusDto;
import com.crud.library.exception.BookNotFoundException;
import com.crud.library.exception.BookStatusDoesNotExistException;
import com.crud.library.exception.BookTitleNotFoundException;
import com.crud.library.mapper.BookMapper;
import com.crud.library.service.BookDbService;
import com.crud.library.service.BookTitleDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookTitleDbService bookTitleDbService;
    private final BookDbService bookDbService;
    private final BookMapper bookMapper;

    @GetMapping(value = "{bookId}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long bookId) throws BookNotFoundException {
        return ResponseEntity.ok(bookMapper.mapToBookDto(bookDbService.getBook(bookId)));
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<Book> books = bookDbService.getAllBooks();
        return ResponseEntity.ok(bookMapper.mapToBookDtoList(books));
    }

    @GetMapping(value = "status/{status}")
    public ResponseEntity<List<BookDto>> getBooksByStatus(@PathVariable String status)
            throws BookStatusDoesNotExistException {
        List<Book> books = bookDbService.getBooksByStatus(status);
        return ResponseEntity.ok(bookMapper.mapToBookDtoList(books));
    }

    @GetMapping(value = "status/{status}/count")
    public ResponseEntity<Long> getBooksCountByStatus(@PathVariable String status)
            throws BookStatusDoesNotExistException {
        long booksCount = bookDbService.getBooksCountByStatus(status);
        return ResponseEntity.ok(booksCount);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> addNewBook(@RequestBody CreateBookDto createBookDto)
            throws BookTitleNotFoundException {
        BookTitle bookTitle = bookTitleDbService.getBookTitle(createBookDto.getBookTitleId());
        Book book = bookMapper.mapToCreatedBook(createBookDto, bookTitle);
        bookDbService.saveBook(book);
        return ResponseEntity.ok(book.getBookId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> updateBook(@RequestBody CreateBookDto createBookDto)
            throws BookTitleNotFoundException, BookNotFoundException {
        BookTitle bookTitle = bookTitleDbService.getBookTitle(createBookDto.getBookTitleId());
        Book book = bookMapper.mapToCreatedBook(createBookDto, bookTitle);
        Book savedBook = bookDbService.updateBook(book);
        return ResponseEntity.ok(bookMapper.mapToBookDto(savedBook));
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> updateBookStatus(@RequestBody UpdateBookStatusDto updateBookStatusDto)
            throws BookNotFoundException, BookTitleNotFoundException {
        Long bookId = updateBookStatusDto.getBookId();
        Book updateBook = bookDbService.getBook(bookId);
        Long bookTitleId = updateBook.getBookTitle().getBookTitleId();
        BookTitle bookTitle = bookTitleDbService.getBookTitle(bookTitleId);
        Book book = bookMapper.mapToUpdateBookStatus(updateBookStatusDto, bookTitle);
        Book savedBook = bookDbService.updateBook(book);
        return ResponseEntity.ok(bookMapper.mapToBookDto(savedBook));
    }

    @DeleteMapping(value = "{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookDbService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }
}
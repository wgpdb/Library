package com.crud.library.mapper;

import com.crud.library.domain.Book;
import com.crud.library.domain.BookTitle;
import com.crud.library.domain.dto.BookDto;
import com.crud.library.domain.dto.CreateBookDto;
import com.crud.library.domain.dto.UpdateBookStatusDto;
import com.crud.library.exception.BookTitleNotFoundException;
import com.crud.library.service.BookTitleDbService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookMapper {

    private BookTitleDbService bookTitleDbService;

    public Book mapToBook(final BookDto bookDto) throws BookTitleNotFoundException {
        return new Book(
                bookDto.getBookId(),
                bookTitleDbService.getBookTitle(bookDto.getBookTitleId()),
                bookDto.getBookStatus()
        );
    }

    public Book mapToCreatedBook(final CreateBookDto createBookDto, final BookTitle bookTitle) {
        return new Book(
                createBookDto.getBookId(),
                bookTitle,
                createBookDto.getBookStatus()
        );
    }

    public Book mapToUpdateBookStatus(final UpdateBookStatusDto updateBookStatusDto, final BookTitle bookTitle) {
        return new Book(
                updateBookStatusDto.getBookId(),
                bookTitle,
                updateBookStatusDto.getBookStatus()
        );
    }

    public BookDto mapToBookDto(final Book book) {
        return new BookDto(
                book.getBookId(),
                book.getBookTitle().getBookTitleId(),
                book.getBookStatus()
        );
    }

    public List<BookDto> mapToBookDtoList(final List<Book> bookList) {
        return bookList.stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }
}
package com.crud.library.mapper;

import com.crud.library.domain.BookTitle;
import com.crud.library.domain.dto.BookTitleDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookTitleMapper {

    public BookTitle mapToBookTitle(final BookTitleDto bookTitleDto) {
        return new BookTitle(
                bookTitleDto.getBookTitleId(),
                bookTitleDto.getBookTitle(),
                bookTitleDto.getBookAuthor(),
                bookTitleDto.getYearPublished(),
                bookTitleDto.getBooks()
        );
    }

    public BookTitleDto mapToBookTitleDto(final BookTitle bookTitle) {
        return new BookTitleDto(
                bookTitle.getBookTitleId(),
                bookTitle.getBookTitle(),
                bookTitle.getBookAuthor(),
                bookTitle.getYearPublished(),
                bookTitle.getBooks()
        );
    }

    public List<BookTitleDto> mapToBookTitleDtoList(final List<BookTitle> bookTitleList) {
        return bookTitleList.stream()
                .map(this::mapToBookTitleDto)
                .collect(Collectors.toList());
    }
}
package com.crud.library.controller;

import com.crud.library.domain.BookTitle;
import com.crud.library.domain.dto.BookTitleDto;
import com.crud.library.exception.BookTitleNotFoundException;
import com.crud.library.mapper.BookTitleMapper;
import com.crud.library.service.BookTitleDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/booktitles")
@RequiredArgsConstructor
public class BookTitleController {

    private final BookTitleDbService service;
    private final BookTitleMapper bookTitleMapper;

    @GetMapping
    public ResponseEntity<List<BookTitleDto>> getAllBookTitles() {
        List<BookTitle> bookTitles = service.getAllBookTitles();
        return ResponseEntity.ok(bookTitleMapper.mapToBookTitleDtoList(bookTitles));
    }

    @GetMapping(value = "{bookTitleId}")
    public ResponseEntity<BookTitleDto> getBookTitle(@PathVariable Long bookTitleId) throws BookTitleNotFoundException {
        return ResponseEntity.ok(bookTitleMapper.mapToBookTitleDto(service.getBookTitle(bookTitleId)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewBookTitle(@RequestBody BookTitleDto bookTitleDto) {
        BookTitle bookTitle = bookTitleMapper.mapToBookTitle(bookTitleDto);
        service.saveBookTitle(bookTitle);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<BookTitleDto> updateBookTitle(@RequestBody BookTitleDto bookTitleDto) {
        BookTitle book = bookTitleMapper.mapToBookTitle(bookTitleDto);
        BookTitle savedBook = service.saveBookTitle(book);
        return ResponseEntity.ok(bookTitleMapper.mapToBookTitleDto(savedBook));
    }

    @DeleteMapping(value = "{bookTitleId}")
    public ResponseEntity<Void> deleteBookTitle(@PathVariable Long bookTitleId) {
        service.deleteBookTitle(bookTitleId);
        return ResponseEntity.ok().build();
    }
}
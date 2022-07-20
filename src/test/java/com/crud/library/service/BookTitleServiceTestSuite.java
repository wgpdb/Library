package com.crud.library.service;

import com.crud.library.domain.Book;
import com.crud.library.domain.BookStatus;
import com.crud.library.domain.BookTitle;
import com.crud.library.exception.BookNotFoundException;
import com.crud.library.exception.BookTitleNotFoundException;
import com.crud.library.repository.BookTitleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class BookTitleServiceTestSuite {

    @Autowired
    private BookTitleDbService bookTitleDbService;

    @Autowired
    private BookTitleRepository bookTitleRepository;

    @Container
    private static MySQLContainer container = new MySQLContainer("mysql:latest");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @AfterEach
    void cleanup() {
        bookTitleRepository.deleteAll();
    }

    @Test
    void testGetBook() throws BookTitleNotFoundException {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        Long id = bookTitle.getBookTitleId();

        //Then
        assertNotNull(bookTitleDbService.getBookTitle(id));
        assertEquals("Title", bookTitleDbService.getBookTitle(id).getBookTitle());
        assertEquals("Author", bookTitleDbService.getBookTitle(id).getBookAuthor());
        assertEquals(2010, bookTitleDbService.getBookTitle(id).getYearPublished());
    }

    @Test
    void testBookTitleNotFoundException() {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        bookTitleDbService.saveBookTitle(bookTitle);
        Long id = bookTitle.getBookTitleId();

        //When
        Long nonexistentId = id + 1;

        //Then
        assertTrue(bookTitleRepository.existsById(id));
        assertThrows(BookTitleNotFoundException.class, () -> bookTitleDbService.getBookTitle(nonexistentId));
    }

    @Test
    void testGetAllBookTitles() {
        //Given
        BookTitle bookTitleOne = BookTitle.builder()
                .bookTitle("Title One")
                .bookAuthor("Author One")
                .yearPublished(2010)
                .build();

        BookTitle bookTitleTwo = BookTitle.builder()
                .bookTitle("Title Two")
                .bookAuthor("Author Two")
                .yearPublished(1999)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitleOne);
        bookTitleDbService.saveBookTitle(bookTitleTwo);

        //Then
        assertEquals(2, bookTitleDbService.getAllBookTitles().size());
    }

    @Test
    void testDeleteBookTitle() {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        bookTitleDbService.saveBookTitle(bookTitle);
        Long id = bookTitle.getBookTitleId();

        //When
        bookTitleDbService.deleteBookTitle(id);

        //Then
        assertFalse(bookTitleRepository.existsById(id));
    }
}
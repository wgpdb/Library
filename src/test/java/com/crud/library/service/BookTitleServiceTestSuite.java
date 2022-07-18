package com.crud.library.service;

import com.crud.library.domain.BookTitle;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class BookTitleServiceTestSuite {

    @Autowired
    BookTitleDbService bookTitleDbService;

    @Autowired
    BookTitleRepository bookTitleRepository;

    @Container
    private static MySQLContainer container = new MySQLContainer("mysql:8.0.29");

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
        BookTitle book = new BookTitle(
                1L,
                "Title",
                "Author",
                2010,
                null);

        //When
        bookTitleDbService.saveBookTitle(book);
        Long id = book.getBookTitleId();

        //Then
        assertNotNull(bookTitleDbService.getBookTitle(id));
        assertEquals("Title", bookTitleDbService.getBookTitle(id).getBookTitle());
        assertEquals("Author", bookTitleDbService.getBookTitle(id).getBookAuthor());
        assertEquals(2010, bookTitleDbService.getBookTitle(id).getYearPublished());
    }

    @Test
    void testGetAllBookTitles() {
        //Given
        BookTitle bookOne = new BookTitle(
                2L,
                "Title One",
                "Author",
                2010,
                null
        );

        BookTitle bookTwo = new BookTitle(
                3L,
                "Title Two",
                "Author",
                1999,
                null
        );

        //When
        bookTitleDbService.saveBookTitle(bookOne);
        bookTitleDbService.saveBookTitle(bookTwo);

        //Then
        assertEquals(2, bookTitleDbService.getAllBookTitles().size());
    }
}
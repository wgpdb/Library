package com.crud.library.service;

import com.crud.library.domain.Book;
import com.crud.library.domain.BookStatus;
import com.crud.library.domain.BookTitle;
import com.crud.library.exception.BookNotFoundException;
import com.crud.library.exception.BookStatusDoesNotExistException;
import com.crud.library.repository.BookRepository;
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
public class BookServiceTestSuite {

    @Autowired
    private BookDbService bookDbService;

    @Autowired
    private BookRepository bookRepository;

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
        bookRepository.deleteAll();
        bookTitleRepository.deleteAll();
    }

    @Test
    void testGetBook() throws BookNotFoundException {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book book = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(book);
        Long id = book.getBookId();

        //Then
        assertTrue(bookRepository.existsById(id));
        assertEquals("Title", bookDbService.getBook(id).getBookTitle().getBookTitle());
        assertEquals(BookStatus.AVAILABLE, bookDbService.getBook(id).getBookStatus());
    }

    @Test
    void testBookNotFoundException() {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book book = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(book);
        Long id = book.getBookId();

        //When
        Long nonexistentId = id + 1;

        //Then
        assertTrue(bookRepository.existsById(id));
        assertThrows(BookNotFoundException.class, () -> bookDbService.getBook(nonexistentId));
    }

    @Test
    void testGetAllBooks() {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book bookOne = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book bookTwo = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(bookOne);
        bookDbService.saveBook(bookTwo);

        //Then
        assertEquals(2, bookDbService.getAllBooks().size());
    }

    @Test
    void testGetBookStatusById() throws BookNotFoundException {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book book = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(book);
        Long id = book.getBookId();

        //Then
        assertTrue(bookDbService.getBookStatusById(id).equals(BookStatus.AVAILABLE));
    }

    @Test
    void testGetBooksByStatus() throws BookStatusDoesNotExistException {
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book bookOne = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book bookTwo = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book bookThree = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.ISSUED)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(bookOne);
        bookDbService.saveBook(bookTwo);
        bookDbService.saveBook(bookThree);

        //Then
        assertEquals(2, bookDbService.getBooksByStatus("Available").size());
        assertEquals(1, bookDbService.getBooksByStatus("Issued").size());
        assertThrows(BookStatusDoesNotExistException.class, () -> bookDbService.getBooksByStatus("Something"));
    }

    @Test
    void testGetBooksCountByStatus() throws BookStatusDoesNotExistException {
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book bookOne = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book bookTwo = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book bookThree = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.ISSUED)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(bookOne);
        bookDbService.saveBook(bookTwo);
        bookDbService.saveBook(bookThree);

        //Then
        assertEquals(2, bookDbService.getBooksCountByStatus("Available"));
        assertEquals(1, bookDbService.getBooksCountByStatus("Issued"));
        assertEquals(0, bookDbService.getBooksCountByStatus("Damaged"));
    }

    @Test
    void testUpdateBook() throws BookNotFoundException {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book book = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book bookUpdate = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.DAMAGED)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(book);
        Long id = book.getBookId();
        bookUpdate.setBookId(id);
        bookDbService.updateBook(bookUpdate);

        //Then
        assertEquals(BookStatus.DAMAGED, bookDbService.getBook(id).getBookStatus());
    }

    @Test
    void testDeleteBook() {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book book = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(book);
        Long id = book.getBookId();

        //When
        bookDbService.deleteBook(id);

        //Then
        assertFalse(bookRepository.existsById(id));
    }

    @Test
    void testDeleteBookDoesNotDeleteBookTitle() {
        //Given
        BookTitle bookTitle = BookTitle.builder()
                .bookTitle("Title")
                .bookAuthor("Author")
                .yearPublished(2010)
                .build();

        Book book = Book.builder()
                .bookTitle(bookTitle)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        bookTitleDbService.saveBookTitle(bookTitle);
        Long bookTitleId = bookTitle.getBookTitleId();
        bookDbService.saveBook(book);
        Long bookId = book.getBookId();

        //When
        bookDbService.deleteBook(bookId);

        //Then
        assertFalse(bookRepository.existsById(bookId));
        assertTrue(bookTitleRepository.existsById(bookTitleId));
    }
}
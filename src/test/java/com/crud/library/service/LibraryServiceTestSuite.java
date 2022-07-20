package com.crud.library.service;

import com.crud.library.domain.*;
import com.crud.library.exception.BookIssueNotFoundException;
import com.crud.library.repository.BookRepository;
import com.crud.library.repository.BookTitleRepository;
import com.crud.library.repository.LibraryRepository;
import com.crud.library.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class LibraryServiceTestSuite {

    @Autowired
    private LibraryDbService libraryDbService;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookTitleDbService bookTitleDbService;

    @Autowired
    private BookTitleRepository bookTitleRepository;

    @Autowired
    private BookDbService bookDbService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberDbService memberDbService;

    @Autowired
    private MemberRepository memberRepository;

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
        libraryRepository.deleteAll();
        memberRepository.deleteAll();
        bookRepository.deleteAll();
        bookTitleRepository.deleteAll();
    }

    @Test
    void testGetIssuedBook() throws BookIssueNotFoundException {
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

        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        Library bookIssue = Library.builder()
                .book(book)
                .member(member)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        book.setBookStatus(BookStatus.ISSUED);
        bookDbService.saveBook(book);
        memberDbService.saveMember(member);
        libraryDbService.saveBookIssue(bookIssue);
        Long id = bookIssue.getLibraryId();

        //Then
        assertNotNull(libraryDbService.getIssuedBook(id));
        assertEquals("Title", libraryDbService.getIssuedBook(id).getBook().getBookTitle().getBookTitle());
        assertEquals("Author", libraryDbService.getIssuedBook(id).getBook().getBookTitle().getBookAuthor());
        assertEquals(2010, libraryDbService.getIssuedBook(id).getBook().getBookTitle().getYearPublished());
        assertEquals(BookStatus.ISSUED, libraryDbService.getIssuedBook(id).getBook().getBookStatus());
        assertEquals("John", libraryDbService.getIssuedBook(id).getMember().getFirstName());
        assertEquals("Smith", libraryDbService.getIssuedBook(id).getMember().getLastName());
        assertNotNull(libraryDbService.getIssuedBook(id).getIssueDate());
        assertNull(libraryDbService.getIssuedBook(id).getReturnDate());
    }

    @Test
    void testBookIssueNotFoundException() {
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

        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        Library bookIssue = Library.builder()
                .book(book)
                .member(member)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        book.setBookStatus(BookStatus.ISSUED);
        bookDbService.saveBook(book);
        memberDbService.saveMember(member);
        libraryDbService.saveBookIssue(bookIssue);
        Long id = bookIssue.getLibraryId();

        //When
        Long nonexistentId = id + 1;

        //Then
        assertTrue(libraryRepository.existsById(id));
        assertThrows(BookIssueNotFoundException.class, () -> libraryDbService.getIssuedBook(nonexistentId));
    }

    @Test
    void testGetIssuedBooks() {
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

        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        Library bookIssueOne = Library.builder()
                .book(bookOne)
                .member(member)
                .build();

        Library bookIssueTwo = Library.builder()
                .book(bookTwo)
                .member(member)
                .build();

        //When
        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(bookOne);
        bookDbService.saveBook(bookTwo);
        memberDbService.saveMember(member);
        libraryDbService.saveBookIssue(bookIssueOne);
        libraryDbService.saveBookIssue(bookIssueTwo);

        //Then
        assertEquals(2, libraryDbService.getAllIssuedBooks().size());
    }

    @Test
    void testSaveBookReturn() throws BookIssueNotFoundException {
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

        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        Library bookIssue = Library.builder()
                .book(book)
                .member(member)
                .build();

        bookTitleDbService.saveBookTitle(bookTitle);
        book.setBookStatus(BookStatus.ISSUED);
        bookDbService.saveBook(book);
        memberDbService.saveMember(member);
        libraryDbService.saveBookIssue(bookIssue);
        Long id = bookIssue.getLibraryId();

        //When
        book.setBookStatus(BookStatus.AVAILABLE);
        bookIssue.setReturnDate(LocalDate.now());
        bookDbService.saveBook(book);
        libraryDbService.saveBookReturn(bookIssue);

        //Then
        assertEquals(BookStatus.AVAILABLE, libraryDbService.getIssuedBook(id).getBook().getBookStatus());
        assertNotNull(libraryDbService.getIssuedBook(id).getReturnDate());
    }

    @Test
    void testDeleteBookIssue() {
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

        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        Library bookIssue = Library.builder()
                .book(book)
                .member(member)
                .build();

        bookTitleDbService.saveBookTitle(bookTitle);
        bookDbService.saveBook(book);
        memberDbService.saveMember(member);
        libraryDbService.saveBookIssue(bookIssue);
        Long id = bookIssue.getLibraryId();

        //When
        libraryDbService.deleteBookIssue(id);

        //Then
        assertFalse(libraryRepository.existsById(id));
    }
}
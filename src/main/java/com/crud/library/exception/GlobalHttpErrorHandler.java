package com.crud.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookTitleNotFoundException.class)
    public ResponseEntity<Object> handleBookTitleNotFoundException(BookTitleNotFoundException exception) {
        return new ResponseEntity<>("Book title with provided id does not exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException exception) {
        return new ResponseEntity<>("Book with provided id does not exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookStatusDoesNotExistException.class)
    public ResponseEntity<Object> handleBookStatusDoesNotExistException(BookStatusDoesNotExistException exception) {
        return new ResponseEntity<>("Provided book status does not exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<Object> handleBookNotAvailableException(BookNotAvailableException exception) {
        return new ResponseEntity<>("Book with provided id is not available", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookIssueNotFoundException.class)
    public ResponseEntity<Object> handleBookIssueNotFoundException(BookIssueNotFoundException exception) {
        return new ResponseEntity<>("Book issue with provided id does not exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotIssuedException.class)
    public ResponseEntity<Object> handleBookNotIssuedException(BookNotIssuedException exception) {
        return new ResponseEntity<>("Book with provided id has not been issued", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Object> handleMemberNotFoundException(MemberNotFoundException exception) {
        return new ResponseEntity<>("Member with provided id does not exist", HttpStatus.BAD_REQUEST);
    }
}
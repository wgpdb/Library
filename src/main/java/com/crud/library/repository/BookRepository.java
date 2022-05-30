package com.crud.library.repository;

import com.crud.library.domain.Book;
import com.crud.library.domain.BookStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Override
    List<Book> findAll();

    @Override
    Optional<Book> findById(Long id);

    List<Book> findByBookStatus(BookStatus bookStatus);

    long countBooksByBookStatus(BookStatus bookStatus);
}
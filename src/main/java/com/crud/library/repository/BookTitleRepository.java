package com.crud.library.repository;

import com.crud.library.domain.BookTitle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookTitleRepository extends CrudRepository<BookTitle, Long> {

    @Override
    List<BookTitle> findAll();

    @Override
    Optional<BookTitle> findById(Long id);
}
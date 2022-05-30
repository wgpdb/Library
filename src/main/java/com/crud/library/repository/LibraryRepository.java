package com.crud.library.repository;

import com.crud.library.domain.Library;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryRepository extends CrudRepository<Library, Long> {

    @Override
    List<Library> findAll();

    @Override
    Optional<Library> findById(Long id);
}
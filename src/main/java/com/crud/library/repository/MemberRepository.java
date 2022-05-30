package com.crud.library.repository;

import com.crud.library.domain.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    @Override
    List<Member> findAll();

    @Override
    Optional<Member> findById(Long id);
}

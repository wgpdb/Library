package com.crud.library.domain.dto;

import com.crud.library.domain.Library;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberDto {

    private Long memberId;
    private String firstName;
    private String lastName;
    private LocalDate dateAccountCreated;
    private List<Library> library;
}
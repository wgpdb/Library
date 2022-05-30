package com.crud.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "book_titles")
public class BookTitle {

    @Id
    @GeneratedValue
    @Column(name = "book_title_id")
    private Long bookTitleId;

    @Column(name = "title")
    private String bookTitle;

    @Column(name = "author")
    private String bookAuthor;

    @Column(name = "year_published")
    private int yearPublished;

    @JsonIgnore
    @OneToMany(
            targetEntity = Book.class,
            mappedBy = "bookTitle",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Book> books = new ArrayList<>();
}
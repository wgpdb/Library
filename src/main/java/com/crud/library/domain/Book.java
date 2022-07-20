package com.crud.library.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue
    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne/*(cascade = CascadeType.ALL)*/
    @JoinColumn(name = "book_title_id")
    private BookTitle bookTitle;

    @Column(name = "book_status")
    private BookStatus bookStatus;
}
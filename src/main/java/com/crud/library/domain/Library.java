package com.crud.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "issued_books")
public class Library {

    @Id
    @GeneratedValue
    @Column(name = "book_issue_id")
    private Long libraryId;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "date_issued")
    @CreationTimestamp
    private LocalDate issueDate;

    @Column(name = "date_returned")
    private LocalDate returnDate;
}
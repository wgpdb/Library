package com.crud.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_account_created", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate dateAccountCreated;

    @JsonIgnore
    @OneToMany(
            targetEntity = Library.class,
            mappedBy = "member",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Library> library = new ArrayList<>();
}
package com.library.librarysystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotBlank
    private String description;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "book_authors",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "authors_id")})
    private List<Author> authors = new ArrayList<>();

    @NotBlank
    private String publisher;

    @NotNull
    private Date dateOfPublication;

    @NotNull
    private Integer numberOfPages;

}

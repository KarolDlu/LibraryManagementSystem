package com.library.librarysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Lob
    @NotBlank
    private String description;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_authors",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "authors_id")})
    private List<Author> authors = new ArrayList<>();

    @NotBlank
    private String publisher;

    @NotNull
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date dateOfPublication;

    @NotNull
    private Integer numberOfPages;

    @JsonIgnore
    @OneToMany(mappedBy = "book",cascade = CascadeType.REMOVE)
    private List<BookItem> bookItem = new ArrayList<>();

}

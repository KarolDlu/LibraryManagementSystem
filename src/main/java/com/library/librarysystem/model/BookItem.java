package com.library.librarysystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BookItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @NotNull
    private Date dateOfPurchase;

}

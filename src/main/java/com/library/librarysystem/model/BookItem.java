package com.library.librarysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date dateOfPurchase;

    @NotNull
    private Double price;

    public BookItem(@NotNull Book book, BookStatus bookStatus, @NotNull Date dateOfPurchase, @NotNull Double price) {
        this.book = book;
        this.bookStatus = bookStatus;
        this.dateOfPurchase = dateOfPurchase;
        this.price = price;
    }
}

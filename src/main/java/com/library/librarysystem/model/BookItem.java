package com.library.librarysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate dateOfPurchase;

    @NotNull
    private Double price;

    public BookItem(@NotNull Book book, BookStatus bookStatus, LocalDate dateOfPurchase, @NotNull Double price) {
        this.book = book;
        this.bookStatus = bookStatus;
        this.dateOfPurchase = dateOfPurchase;
        this.price = price;
    }

    public void borrow(){
        this.bookStatus = BookStatus.LOANED;
    }

    public void returnBook(){
        this.bookStatus = BookStatus.AVAILABLE;
    }
}

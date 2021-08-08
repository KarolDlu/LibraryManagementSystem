package com.library.librarysystem.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.library.librarysystem.model.Book;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookItemDTO {

    @NotNull
    private Book book;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfPurchase;

    @NotNull
    private Double price;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

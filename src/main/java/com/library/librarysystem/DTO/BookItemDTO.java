package com.library.librarysystem.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.library.librarysystem.model.Book;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class BookItemDTO {

    @NotNull
    private Book book;
    @NotNull
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date dateOfPurchase;
    @NotNull
    private Double price;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

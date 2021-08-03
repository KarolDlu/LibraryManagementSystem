package com.library.librarysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BookLending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private BookItem bookItem;

    @ManyToOne
    private MemberAccount memberAccount;

    @NotNull
    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate creationDate;

    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate dueDate;

    public BookLending(BookItem bookItem, MemberAccount memberAccount, LocalDate creationDate, @NotNull LocalDate dueDate) {
        this.bookItem = bookItem;
        this.memberAccount = memberAccount;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
    }
}

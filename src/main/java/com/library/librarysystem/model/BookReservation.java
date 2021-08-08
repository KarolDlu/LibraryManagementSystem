package com.library.librarysystem.model;

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
public class BookReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne
    private MemberAccount memberAccount;

    @NotNull
    private LocalDate reservationDate;

    public BookReservation(Book book, ReservationStatus status, MemberAccount memberAccount, @NotNull LocalDate reservationDate) {
        this.book = book;
        this.reservationStatus = status;
        this.memberAccount = memberAccount;
        this.reservationDate = reservationDate;
    }

    public void changeStatus(ReservationStatus reservationStatus){
        this.reservationStatus = reservationStatus;
    }
}

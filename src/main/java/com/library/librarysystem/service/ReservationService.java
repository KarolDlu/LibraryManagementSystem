package com.library.librarysystem.service;

import com.library.librarysystem.model.BookReservation;

public interface ReservationService {

    BookReservation makeBookReservation(Long memberId, Long bookId);

    boolean cancelReservation(Long memberId, Long bookId);
}

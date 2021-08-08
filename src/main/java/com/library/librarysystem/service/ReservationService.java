package com.library.librarysystem.service;

import com.library.librarysystem.model.BookLending;
import com.library.librarysystem.model.BookReservation;
import com.library.librarysystem.model.MemberAccount;

public interface ReservationService {

    BookReservation makeBookReservation(Long memberId, Long bookId);

    BookLending completeReservation(Long memberId, Long bookId);

    boolean cancelReservation(Long memberId, Long bookId);

    boolean checkIfBookIsReservedByMember(Long memberId, Long bookId);
}

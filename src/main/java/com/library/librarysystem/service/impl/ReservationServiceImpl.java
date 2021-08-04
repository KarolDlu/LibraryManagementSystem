package com.library.librarysystem.service.impl;

import com.library.librarysystem.config.Constants;
import com.library.librarysystem.model.*;
import com.library.librarysystem.repository.BookItemRepo;
import com.library.librarysystem.repository.BookReservationRepo;
import com.library.librarysystem.repository.MemberAccountRepo;
import com.library.librarysystem.service.BookService;
import com.library.librarysystem.service.ReservationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    BookReservationRepo bookReservationRepo;
    MemberAccountRepo memberAccountRepo;
    BookItemRepo bookItemRepo;
    BookService bookService;

    public ReservationServiceImpl(BookReservationRepo bookReservationRepo, MemberAccountRepo memberAccountRepo, BookItemRepo bookItemRepo, BookServiceImpl bookService) {
        this.bookReservationRepo = bookReservationRepo;
        this.memberAccountRepo = memberAccountRepo;
        this.bookItemRepo = bookItemRepo;
        this.bookService = bookService;
    }

    @Override
    public BookReservation makeBookReservation(Long memberId, Long bookId) {
        Optional<MemberAccount> member = memberAccountRepo.findById(memberId);
        if (member.isPresent()) {
            List<BookReservation> reservations = bookReservationRepo.findBookReservationsByMemberAccount_Id(memberId);
            if (reservations.size() < Constants.MAX_NUMBER_OF_RESERVATIONS) {
                Book book = bookService.getBookById(bookId);
                return bookReservationRepo.save(new BookReservation(book, member.get(), LocalDate.now()));
            }
            return null; // change to throw statement
        }
        return null; // throw member does not exist
    }

    @Override
    public boolean cancelReservation(Long memberId, Long bookId) {

        Optional<BookReservation> optReservation = bookReservationRepo.findBookReservationByMemberAccount_IdAndBook_BookId(memberId, bookId);
        if (optReservation.isPresent()) {
            BookReservation reservation = optReservation.get();
            if (reservation.getReservationStatus().equals(ReservationStatus.WAITING)) {
                reservation.changeStatus(ReservationStatus.CANCELED);
                bookReservationRepo.save(reservation);
                return true;
            }
            if (reservation.getReservationStatus().equals(ReservationStatus.PENDING)) {
                reservation.changeStatus(ReservationStatus.CANCELED);
                bookReservationRepo.save(reservation);
                Optional<BookReservation> optWaitingReservation = bookReservationRepo.findBookReservationsByBookAndReservationStatus_WaitingAndOrderByReservationDateAsc(reservation.getBook());
                if (optWaitingReservation.isPresent()) {
                    BookReservation waitingReservation = optWaitingReservation.get();
                    waitingReservation.changeStatus(ReservationStatus.PENDING);
                    bookReservationRepo.save(waitingReservation);
                } else {
                    Optional<BookItem> bookItem = bookItemRepo.findBookItemByBookAndBookStatus_Reserved(reservation.getBook());
                    if (bookItem.isPresent()) {
                        BookItem reservedBook = bookItem.get();
                        reservedBook.cancelReservation();
                        bookItemRepo.save(reservedBook);
                    }
                }
            }
        }
        return false;
    }
}

package com.library.librarysystem.service.impl;

import com.library.librarysystem.config.Constants;
import com.library.librarysystem.model.*;
import com.library.librarysystem.repository.BookItemRepo;
import com.library.librarysystem.repository.BookLendingRepo;
import com.library.librarysystem.repository.BookReservationRepo;
import com.library.librarysystem.repository.MemberAccountRepo;
import com.library.librarysystem.service.BookService;
import com.library.librarysystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    BookReservationRepo bookReservationRepo;
    MemberAccountRepo memberAccountRepo;
    BookLendingRepo bookLendingRepo;
    BookItemRepo bookItemRepo;
    BookService bookService;

    @Autowired
    public ReservationServiceImpl(BookReservationRepo bookReservationRepo, MemberAccountRepo memberAccountRepo, BookLendingRepo bookLendingRepo, BookItemRepo bookItemRepo, BookServiceImpl bookService) {
        this.bookReservationRepo = bookReservationRepo;
        this.memberAccountRepo = memberAccountRepo;
        this.bookLendingRepo = bookLendingRepo;
        this.bookItemRepo = bookItemRepo;
        this.bookService = bookService;
    }

    @Override
    public BookReservation makeBookReservation(Long memberId, Long bookId) {
        Optional<BookReservation> reservation = bookReservationRepo.findBookReservationByMemberAccount_IdAndBook_BookIdAndReservationStatusNotIn(memberId, bookId, Arrays.asList(new ReservationStatus[]{ReservationStatus.CANCELED, ReservationStatus.COMPLETED}));
        if (!reservation.isPresent()) {
            Optional<MemberAccount> member = memberAccountRepo.findById(memberId);
            Book book = bookService.getBookById(bookId);
            if (member.isPresent()) {
                List<BookReservation> reservations = bookReservationRepo.findBookReservationsByMemberAccount_Id(memberId);
                if (reservations.size() < Constants.MAX_NUMBER_OF_RESERVATIONS) {
                    List<BookItem> bookItems = bookItemRepo.findBookItemsByBookAndBookStatus(book, BookStatus.AVAILABLE);
                    if (bookItems.size() > 0) {
                        BookItem bookItem = bookItems.get(0);
                        bookItem.reserve();
                        bookItemRepo.save(bookItem);
                        return bookReservationRepo.save(new BookReservation(book, ReservationStatus.PENDING, member.get(), LocalDate.now()));
                    }
                    return bookReservationRepo.save(new BookReservation(book, ReservationStatus.WAITING, member.get(), LocalDate.now()));
                }
                return null; // change to throw statement
            }
            return null; // throw member does not exist
        }
        return null; // throw reservation already exist
    }

    @Override
    public BookLending completeReservation(Long memberId, Long bookId) {
        Optional<BookReservation> optReservation = bookReservationRepo.findBookReservationByMemberAccount_IdAndBook_BookIdAndReservationStatus(memberId, bookId, ReservationStatus.PENDING);
        if (optReservation.isPresent()) {
            Optional<MemberAccount> optMember = memberAccountRepo.findById(memberId);
            if (optMember.isPresent()) {
                MemberAccount member = optMember.get();
                Book book = bookService.getBookById(bookId);
                List<BookItem> bookItems = bookItemRepo.findBookItemsByBookAndBookStatus(book, BookStatus.RESERVED);
                BookItem reservedBookItem = bookItems.get(0);
                reservedBookItem.borrow();
                bookItemRepo.save(reservedBookItem);
                BookReservation reservation = optReservation.get();
                reservation.changeStatus(ReservationStatus.COMPLETED);
                bookReservationRepo.save(reservation);
                if (member.isBlacklisted()) {
                    return bookLendingRepo.save(new BookLending(reservedBookItem, member, LocalDate.now(), LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS_FOR_BLACKLISTED)));

                }
                return bookLendingRepo.save(new BookLending(reservedBookItem, member, LocalDate.now(), LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS)));

            }
        }
        return null; // throw reservation not found
    }


    @Override
    public boolean cancelReservation(Long memberId, Long bookId) {

        Optional<BookReservation> optReservation = bookReservationRepo.findBookReservationByMemberAccount_IdAndBook_BookIdAndReservationStatusNotIn(memberId, bookId, Arrays.asList(new ReservationStatus[]{ReservationStatus.CANCELED, ReservationStatus.COMPLETED}));
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
                List<BookReservation> waitingReservations = bookReservationRepo.findBookReservationsByBookAndReservationStatusOrderByReservationDateAsc(reservation.getBook(), ReservationStatus.WAITING);
                if (waitingReservations.size() > 0) {
                    BookReservation waitingReservation = waitingReservations.get(0);
                    waitingReservation.changeStatus(ReservationStatus.PENDING);
                    bookReservationRepo.save(waitingReservation);
                } else {
                    List<BookItem> bookItems = bookItemRepo.findBookItemsByBookAndBookStatus(reservation.getBook(), BookStatus.RESERVED);
                    if (bookItems.size() > 0) {
                        BookItem reservedBook = bookItems.get(0);
                        reservedBook.cancelReservation();
                        bookItemRepo.save(reservedBook);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkIfBookIsReservedByMember(Long memberId, Long bookId) {
        Optional<BookReservation> optReservation = bookReservationRepo.findBookReservationByMemberAccount_IdAndBook_BookIdAndReservationStatus(memberId, bookId, ReservationStatus.PENDING);
        if (optReservation.isPresent()) {
            return true;
        }
        return false;
    }
}

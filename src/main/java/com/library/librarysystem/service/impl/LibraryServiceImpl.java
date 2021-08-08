package com.library.librarysystem.service.impl;

import com.library.librarysystem.config.Constants;
import com.library.librarysystem.model.*;
import com.library.librarysystem.repository.BookItemRepo;
import com.library.librarysystem.repository.BookLendingRepo;
import com.library.librarysystem.repository.BookReservationRepo;
import com.library.librarysystem.repository.MemberAccountRepo;
import com.library.librarysystem.service.LibraryService;
import com.library.librarysystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {

    private MemberAccountRepo memberAccountRepo;
    private BookItemRepo bookItemRepo;
    private BookLendingRepo bookLendingRepo;
    private ReservationService reservationService;
    private BookReservationRepo bookReservationRepo;

    @Autowired
    public LibraryServiceImpl(MemberAccountRepo memberAccountRepo, BookItemRepo bookItemRepo, BookLendingRepo bookLendingRepo, ReservationServiceImpl reservationService, BookReservationRepo bookReservationRepo) {
        this.memberAccountRepo = memberAccountRepo;
        this.bookItemRepo = bookItemRepo;
        this.bookLendingRepo = bookLendingRepo;
        this.reservationService = reservationService;
        this.bookReservationRepo = bookReservationRepo;
    }

    @Override
    public BookLending borrowBook(Long memberId, Long bookItemId) {

        MemberAccount member = checkIfMemberMayBorrowBook(memberId);

        BookItem bookItem = checkIfBookItemIsAvailable(bookItemId);

        if (bookLendingRepo.findBookLendingByMemberAccount_Id(memberId).size() < Constants.MAX_BOOKS_BORROWED_BY_USER) {

            if (reservationService.checkIfBookIsReservedByMember(memberId, bookItem.getBook().getBookId())) {
                return reservationService.completeReservation(memberId, bookItem.getBook().getBookId()); // complete book reservation
            }
            bookItem.borrow();
            bookItemRepo.save(bookItem);
            if (member.isBlacklisted()) {
                BookLending lending = new BookLending(bookItem, member, LocalDate.now(), LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS_FOR_BLACKLISTED));
                return bookLendingRepo.save(lending);
            }
            return bookLendingRepo.save(new BookLending(bookItem, member, LocalDate.now(), LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS)));
        }
        return null; // change to throw statement
    }

    @Override
    public boolean returnBook(Long memberId, Long bookItemId) {
        Optional<BookLending> optBookLending = bookLendingRepo.findBookLendingByMemberAccount_IdAndBookItem_Id(memberId, bookItemId);
        if (optBookLending.isPresent()) {
            BookLending bookLending = optBookLending.get();
            BookItem bookItem = bookItemRepo.getById(bookItemId);

            bookItem.returnBook();
            List<BookReservation> reservations = bookReservationRepo.findBookReservationsByBookAndReservationStatusOrderByReservationDateAsc(bookLending.getBook(), ReservationStatus.WAITING);
            if (reservations.size() > 0) {
                BookReservation reservation = reservations.get(0);
                bookItem.reserve();
                reservation.changeStatus(ReservationStatus.PENDING);
                bookReservationRepo.save(reservation);
            }
            bookItemRepo.save(bookItem);
            bookLendingRepo.delete(bookLending);

            return true;
        }
        return false;
    }

    @Override
    public boolean renewBook(Long memberId, Long bookItemId) {
        Optional<BookLending> optBookLending = bookLendingRepo.findBookLendingByMemberAccount_IdAndBookItem_Id(memberId, bookItemId);
        if (optBookLending.isPresent()) {
            BookLending bookLending = optBookLending.get();
            List<BookReservation> reservations = bookReservationRepo.findBookReservationsByBookAndReservationStatusOrderByReservationDateAsc(bookLending.getBook(), ReservationStatus.WAITING);
            if (reservations.size() == 0) {
                bookLending.renewLending();
                bookLendingRepo.save(bookLending);
                return true;
            }
            BookReservation reservation = reservations.get(0);
            BookItem reservedBookItem = bookLending.getBookItem();
            reservedBookItem.reserve();
            bookItemRepo.save(reservedBookItem);
            reservation.changeStatus(ReservationStatus.PENDING);
            return false; // throw book is reserved
        }
        return false; // throw Lending not found
    }

    private MemberAccount checkIfMemberMayBorrowBook(Long memberId) {
        if (bookLendingRepo.findBookLendingByMemberAccount_Id(memberId).size() > Constants.MAX_BOOKS_BORROWED_BY_USER) {
            return null; // member has borrowed the maximum number of books
        }
        Optional<MemberAccount> optMember = memberAccountRepo.findById(memberId);
        if (optMember.isEmpty()) {
            return null; // change to throw statement
        }
        MemberAccount member = optMember.get();
        if (member.isBlocked()) {
            return null; // change to throw statement
        }
        return member;
    }

    private BookItem checkIfBookItemIsAvailable(Long bookItemId) {
        Optional<BookItem> optBookItem = bookItemRepo.findById(bookItemId);
        if (!optBookItem.isPresent()) {
            return null; // change to throw statement
        }
        BookItem bookItem = optBookItem.get();
        if (!bookItem.getBookStatus().equals(BookStatus.AVAILABLE)) {
            return null; // change to throw statement
        }
        return bookItem;
    }
}

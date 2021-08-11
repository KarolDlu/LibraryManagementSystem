package com.library.librarysystem.service.impl;

import com.library.librarysystem.config.Constants;
import com.library.librarysystem.exceptions.*;
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
                return reservationService.completeReservation(memberId, bookItem.getBook().getBookId());
            }
            bookItem.borrow();
            bookItemRepo.save(bookItem);
            if (member.isBlacklisted()) {
                BookLending lending = new BookLending(bookItem, member, LocalDate.now(), LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS_FOR_BLACKLISTED));
                return bookLendingRepo.save(lending);
            }
            return bookLendingRepo.save(new BookLending(bookItem, member, LocalDate.now(), LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS)));
        }
        throw new MaxNumberOfBorrowedBooksReachedException(memberId, Constants.MAX_BOOKS_BORROWED_BY_USER);
    }

    @Override
    public boolean returnBook(Long memberId, Long bookItemId) {
        Optional<BookLending> optBookLending = bookLendingRepo.findBookLendingByMemberAccount_IdAndBookItem_Id(memberId, bookItemId);
        return optBookLending.map(bookLending -> {
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
        }).orElse(false);
    }

    @Override
    public boolean renewBook(Long memberId, Long bookItemId) {
        Optional<BookLending> optBookLending = bookLendingRepo.findBookLendingByMemberAccount_IdAndBookItem_Id(memberId, bookItemId);
        return optBookLending.map(bookLending -> {
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
            throw new BookAlreadyReservedException(reservedBookItem.getBook().getBookId());
        }).orElseThrow(() -> {
            throw new ObjectNotFoundException("BookLending with memberId: (" + memberId + ") and bookItemId: (" + bookItemId + ") does not exists.");
        });
    }

    private MemberAccount checkIfMemberMayBorrowBook(Long memberId) {
        if (bookLendingRepo.findBookLendingByMemberAccount_Id(memberId).size() > Constants.MAX_BOOKS_BORROWED_BY_USER) {
            throw new MaxNumberOfBorrowedBooksReachedException(memberId, Constants.MAX_BOOKS_BORROWED_BY_USER);
        }
        Optional<MemberAccount> optMember = memberAccountRepo.findById(memberId);
        return optMember.map(member -> {
            if (member.isBlocked()) {
                throw new MemberAccountBlockedException(memberId);
            }
            return member;
        }).orElseThrow(() -> {
            throw new ObjectNotFoundException("Member", memberId);
        });
    }

    private BookItem checkIfBookItemIsAvailable(Long bookItemId) {
        Optional<BookItem> optBookItem = bookItemRepo.findById(bookItemId);
        return optBookItem.map(bookItem -> {
            if (!bookItem.getBookStatus().equals(BookStatus.AVAILABLE)) {
                throw new BookNotAvailableException(bookItemId);
            }
            return bookItem;
        }).orElseThrow(() -> {
            throw new ObjectNotFoundException("A copy of book", bookItemId);
        });
    }
}

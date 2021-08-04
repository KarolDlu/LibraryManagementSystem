package com.library.librarysystem.service.impl;

import com.library.librarysystem.config.Constants;
import com.library.librarysystem.model.*;
import com.library.librarysystem.repository.BookItemRepo;
import com.library.librarysystem.repository.BookLendingRepo;
import com.library.librarysystem.repository.BookReservationRepo;
import com.library.librarysystem.repository.MemberAccountRepo;
import com.library.librarysystem.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {

    private MemberAccountRepo memberAccountRepo;
    private BookItemRepo bookItemRepo;
    private BookLendingRepo bookLendingRepo;
    private BookReservationRepo bookReservationRepo;

    @Autowired
    public LibraryServiceImpl(MemberAccountRepo memberAccountRepo, BookItemRepo bookItemRepo, BookLendingRepo bookLendingRepo, BookReservationRepo bookReservationRepo) {
        this.memberAccountRepo = memberAccountRepo;
        this.bookItemRepo = bookItemRepo;
        this.bookLendingRepo = bookLendingRepo;
        this.bookReservationRepo = bookReservationRepo;
    }

    @Override
    public BookLending borrowBook(Long memberId, Long bookItemId) {

        MemberAccount member = checkIfMemberMayBorrowBook(memberId);

        BookItem bookItem = checkIfBookItemIsAvailable(bookItemId);

        Optional<BookReservation> optReservation = bookReservationRepo.findBookReservationByMemberAccount_IdAndBook_BookId(memberId, bookItem.getBook().getBookId());
        if (optReservation.isPresent()){
            BookReservation completedReservation = optReservation.get();
            completedReservation.changeStatus(ReservationStatus.COMPLETED);
            bookReservationRepo.save(completedReservation);
        }
        if (bookLendingRepo.findBookLendingByMemberAccount_Id(memberId).size() < Constants.MAX_BOOKS_BORROWED_BY_USER) {
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
            Optional<BookReservation> optReservation = bookReservationRepo.findBookReservationsByBookAndReservationStatus_WaitingAndOrderByReservationDateAsc(bookLending.getBook());
            if (optReservation.isPresent()) {
                BookReservation reservation = optReservation.get();
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
            Optional<BookReservation> optReservation = bookReservationRepo.findBookReservationsByBookAndReservationStatus_WaitingAndOrderByReservationDateAsc(bookLending.getBook());
            if (!optReservation.isPresent()) {
                bookLending.renewLending();
                bookLendingRepo.save(bookLending);
                return true;
            }
            BookReservation reservation = optReservation.get();
            BookItem reservedBookItem = bookLending.getBookItem();
            reservedBookItem.reserve();
            bookItemRepo.save(reservedBookItem);
            reservation.changeStatus(ReservationStatus.PENDING);
            return false; // throw book is reserved
        }
        return false; // throw Lending not found
    }

    private MemberAccount checkIfMemberMayBorrowBook(Long memberId) {
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
        if (optBookItem.isEmpty()) {
            return null; // change to throw statement
        }
        BookItem bookItem = optBookItem.get();
        if (!bookItem.getBookStatus().equals(BookStatus.AVAILABLE) || !bookItem.getBookStatus().equals(BookStatus.RESERVED)) {
            return null; // change to throw statement
        }
        return bookItem;
    }
}

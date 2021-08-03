package com.library.librarysystem.service.impl;

import com.library.librarysystem.config.Constants;
import com.library.librarysystem.model.BookItem;
import com.library.librarysystem.model.BookLending;
import com.library.librarysystem.model.BookStatus;
import com.library.librarysystem.model.MemberAccount;
import com.library.librarysystem.repository.BookItemRepo;
import com.library.librarysystem.repository.BookLendingRepo;
import com.library.librarysystem.repository.MemberAccountRepo;
import com.library.librarysystem.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {

    private MemberAccountRepo memberAccountRepo;
    private BookItemRepo bookItemRepo;
    private BookLendingRepo bookLendingRepo;

    @Autowired
    public LibraryServiceImpl(MemberAccountRepo memberAccountRepo, BookItemRepo bookItemRepo, BookLendingRepo bookLendingRepo) {
        this.memberAccountRepo = memberAccountRepo;
        this.bookItemRepo = bookItemRepo;
        this.bookLendingRepo = bookLendingRepo;
    }

    @Override
    public BookLending borrowBook(Long memberId, Long bookItemId) {

        MemberAccount member = checkIfMemberMayBorrowBook(memberId);

        BookItem bookItem = checkIfBookItemIsAvailable(bookItemId);

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
            BookItem bookItem = bookItemRepo.getById(bookItemId);

            //TODO book reservation handling

            //TODO fee checking
            BookLending bookLending = optBookLending.get();
            bookLendingRepo.delete(bookLending);
            bookItem.returnBook();
            bookItemRepo.save(bookItem);
            return true;
        }
        return false;
    }

    @Override
    public boolean renewBook(Long memberId, Long bookItemId) {
        return false;
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
        if (!bookItem.getBookStatus().equals(BookStatus.AVAILABLE)) {
            return null; // change to throw statement
        }
        return bookItem;
    }
}

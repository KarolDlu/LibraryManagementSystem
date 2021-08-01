package com.library.librarysystem.service.impl;

import com.library.librarysystem.DTO.BookItemDTO;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BookItem;
import com.library.librarysystem.model.BookStatus;
import com.library.librarysystem.repository.BookItemRepo;
import com.library.librarysystem.repository.BookRepo;
import com.library.librarysystem.service.BookItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookItemServiceImpl implements BookItemService {

    private BookItemRepo bookItemRepo;
    private BookRepo bookRepo;

    @Autowired
    public BookItemServiceImpl(BookItemRepo bookItemRepo, BookRepo bookRepo) {
        this.bookItemRepo = bookItemRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public BookItem addBookItem(BookItemDTO newBookItem) {
        Optional<Book> book = bookRepo.findById(newBookItem.getBook().getBookId());
        if (book.isPresent()) {
            BookItem bookItem = new BookItem(book.get(), BookStatus.AVAILABLE, newBookItem.getDateOfPurchase(), newBookItem.getPrice());
            return bookItemRepo.save(bookItem);
        }
        return null; // add throw statement
    }

    @Override
    public List<BookItem> getAllBookItems() {
        return bookItemRepo.findAll();
    }

    @Override
    public List<BookItem> getBookItemsByBookId(Long bookId) {
        return bookItemRepo.findBookItemsByBook_BookId(bookId);
    }

    @Override
    public BookItem getBookItemById(Long bookItemId) {
        Optional<BookItem> bookItem = bookItemRepo.findById(bookItemId);
        if (bookItem.isPresent()) {
            return bookItem.get();
        }
        return null; // add throw statement
    }

    @Override
    public void deleteBookItem(Long bookItemId) {
        if (bookItemRepo.findById(bookItemId).isPresent()) {
            bookItemRepo.deleteById(bookItemId);
        } else {
            return; // add throw statement
        }
    }
}

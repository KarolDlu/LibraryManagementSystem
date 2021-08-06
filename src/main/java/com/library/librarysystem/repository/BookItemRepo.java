package com.library.librarysystem.repository;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BookItem;
import com.library.librarysystem.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookItemRepo extends JpaRepository<BookItem, Long> {

    List<BookItem> findBookItemsByBook_BookId(Long bookId);

    Optional<BookItem> findBookItemByBookAndBookStatus(Book book, BookStatus status);

    Optional<BookItem> findBookItemByBook_BookIdAndBookStatus(Long bookId, BookStatus status);
}

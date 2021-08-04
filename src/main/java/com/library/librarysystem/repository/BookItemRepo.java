package com.library.librarysystem.repository;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookItemRepo extends JpaRepository<BookItem, Long> {

    List<BookItem> findBookItemsByBook_BookId(Long bookId);

    Optional<BookItem> findBookItemByBookAndBookStatus_Reserved(Book book);

    Optional<BookItem> findBookItemByBook_BookIdAndBookStatus_Available(Long bookId);
}

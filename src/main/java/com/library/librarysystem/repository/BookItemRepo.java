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

    List<BookItem> findBookItemsByBookAndBookStatus(Book book, BookStatus status);

}

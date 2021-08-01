package com.library.librarysystem.repository;

import com.library.librarysystem.model.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookItemRepo extends JpaRepository<BookItem, Long> {

    List<BookItem> findBookItemsByBook_BookId(Long bookId);
}

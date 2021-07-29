package com.library.librarysystem.repository;

import com.library.librarysystem.model.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookItemRepo extends JpaRepository<BookItem, Long> {
}

package com.library.librarysystem.repository;

import com.library.librarysystem.model.BookItem;
import com.library.librarysystem.model.BookLending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookLendingRepo extends JpaRepository<BookLending, Long> {

    List<BookLending> findBookLendingByMemberAccount_Id(Long memberId);
    Optional<BookLending> findBookLendingByMemberAccount_IdAndBookItem_Id(Long memberId, Long bookItemId);
}

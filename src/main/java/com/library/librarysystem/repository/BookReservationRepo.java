package com.library.librarysystem.repository;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.BookReservation;
import com.library.librarysystem.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookReservationRepo extends JpaRepository<BookReservation, Long> {

    List<BookReservation> findBookReservationsByMemberAccount_Id(Long memberId);

    List<BookReservation> findBookReservationsByBook(Book book);

    Optional<BookReservation> findBookReservationByMemberAccount_IdAndBook_BookIdAndReservationStatusNotIn(Long memberId, Long bookId, Collection<ReservationStatus> statuses);

    Optional<BookReservation> findBookReservationByMemberAccount_IdAndBook_BookIdAndReservationStatus(Long memberId, Long bookId, ReservationStatus status);

    List<BookReservation> findBookReservationsByBookAndReservationStatusOrderByReservationDateAsc(Book book, ReservationStatus status);

}

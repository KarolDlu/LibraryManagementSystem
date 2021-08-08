package com.library.librarysystem.controller;

import com.library.librarysystem.model.BookLending;
import com.library.librarysystem.model.BookReservation;
import com.library.librarysystem.service.LibraryService;
import com.library.librarysystem.service.ReservationService;
import com.library.librarysystem.service.impl.LibraryServiceImpl;
import com.library.librarysystem.service.impl.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private LibraryService libraryService;
    private ReservationService reservationService;

    @Autowired
    public LibraryController(LibraryServiceImpl libraryService, ReservationServiceImpl reservationService) {
        this.libraryService = libraryService;
        this.reservationService = reservationService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BookLending> borrowBook(@RequestParam Long memberId, @RequestParam Long bookItemId){
        return new ResponseEntity<>(libraryService.borrowBook(memberId, bookItemId),HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<Boolean> returnBook(@RequestParam Long memberId, @RequestParam Long bookItemId){
        return new ResponseEntity<>(libraryService.returnBook(memberId, bookItemId), HttpStatus.OK);
    }

    @PostMapping("/renew")
    public ResponseEntity<Boolean> renewBookLending(@RequestParam Long memberId, @RequestParam Long bookItemId){
        return new ResponseEntity<>(libraryService.renewBook(memberId, bookItemId), HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<BookReservation> makeBookReservation(@RequestParam Long memberId, @RequestParam Long bookId){
        return new ResponseEntity<>(reservationService.makeBookReservation(memberId, bookId), HttpStatus.OK);
    }

    @PostMapping("/cancel-reservation")
    public ResponseEntity<Boolean> cancelBookReservation(@RequestParam Long memberId, @RequestParam Long bookId){
        return new ResponseEntity<>(reservationService.cancelReservation(memberId, bookId), HttpStatus.OK);
    }

    @PostMapping("/complete-reservation")
    public ResponseEntity<BookLending> completeBookReservation(@RequestParam Long memberId, @RequestParam Long bookId){
        return new ResponseEntity<>(reservationService.completeReservation(memberId, bookId), HttpStatus.OK);
    }
}

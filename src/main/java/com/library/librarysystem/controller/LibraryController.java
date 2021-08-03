package com.library.librarysystem.controller;

import com.library.librarysystem.model.BookLending;
import com.library.librarysystem.service.LibraryService;
import com.library.librarysystem.service.impl.LibraryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryServiceImpl libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BookLending> borrowBook(@RequestParam Long memberId, @RequestParam Long bookItemId){
        return new ResponseEntity<>(libraryService.borrowBook(memberId, bookItemId),HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<Boolean> returnBook(@RequestParam Long memberId, @RequestParam Long bookItemId){
        return new ResponseEntity<>(libraryService.returnBook(memberId, bookItemId), HttpStatus.OK);
    }
}

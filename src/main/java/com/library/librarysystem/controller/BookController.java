package com.library.librarysystem.controller;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.Genre;
import com.library.librarysystem.service.BookService;
import com.library.librarysystem.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book newBook) {
        return new ResponseEntity<>(bookService.addBook(newBook), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Book>> getBooksByGenre(@RequestParam Genre genre) {
        return new ResponseEntity<>(bookService.getBooksByGenre(genre), HttpStatus.OK);
    }

    @GetMapping("/get/author/{id}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBooksByAuthor(id), HttpStatus.OK);
    }

    @GetMapping("/get/title/{title}")
    public ResponseEntity<List<Book>> getBookByTitle(@PathVariable String title) {
        return new ResponseEntity<>(bookService.getBooksByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @GetMapping("/get/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return new ResponseEntity<>(bookService.getBookByIsbn(isbn), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.ok("Book has been deleted.");
    }

}

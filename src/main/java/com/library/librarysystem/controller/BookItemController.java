package com.library.librarysystem.controller;

import com.library.librarysystem.DTO.BookItemDTO;
import com.library.librarysystem.model.BookItem;
import com.library.librarysystem.service.BookItemService;
import com.library.librarysystem.service.impl.BookItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookItem")
public class BookItemController {

    BookItemService bookItemService;

    @Autowired
    public BookItemController(BookItemServiceImpl bookItemService) {
        this.bookItemService = bookItemService;
    }

    @PostMapping("/add")
    public ResponseEntity<BookItem> addBookItem(@RequestBody BookItemDTO bookItemDTO){
        return new ResponseEntity<>(bookItemService.addBookItem(bookItemDTO), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BookItem>> getAllBookItems(){
        return new ResponseEntity<>(bookItemService.getAllBookItems(), HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookItem>> getBookItemsByBookId(@PathVariable Long bookId){
        return new ResponseEntity<>(bookItemService.getBookItemsByBookId(bookId), HttpStatus.OK);
    }

    @GetMapping("/get/{bookItemId}")
    public ResponseEntity<BookItem> getBookItemById(@PathVariable Long bookItemId){
        return new ResponseEntity<>(bookItemService.getBookItemById(bookItemId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{bookItemId}")
    public ResponseEntity<String> deleteBookItemById(@PathVariable Long bookItemId){
        bookItemService.deleteBookItem(bookItemId);
        return new ResponseEntity<>("Success! BookItem has been deleted.", HttpStatus.OK);
    }

}

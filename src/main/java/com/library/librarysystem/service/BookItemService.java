package com.library.librarysystem.service;

import com.library.librarysystem.DTO.BookItemDTO;
import com.library.librarysystem.model.BookItem;

import java.util.List;

public interface BookItemService {

    BookItem addBookItem(BookItemDTO newBookItem);

    List<BookItem> getAllBookItems();

    List<BookItem> getBookItemsByBookId(Long bookId);

    BookItem getBookItemById(Long bookItemId);

    void deleteBookItem(Long bookItemId);

}

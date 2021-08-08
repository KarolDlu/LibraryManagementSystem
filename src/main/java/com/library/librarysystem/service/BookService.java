package com.library.librarysystem.service;

import com.library.librarysystem.model.Author;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.Genre;

import java.util.List;

public interface BookService {

    Book addBook(Book newBook);

    List<Book> getAllBooks();

    List<Book> getBooksByGenre(Genre genre);

    List<Book> getBooksByAuthor(Long id);

    List<Book> getBooksByTitle(String title);

    Book getBookById(Long id);

    Book getBookByIsbn(String isbn);

    void deleteBookById(Long bookId);

    Author addAuthor(Author author);

    void deleteAuthor(Long authorId);

}

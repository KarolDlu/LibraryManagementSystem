package com.library.librarysystem.service.impl;

import com.library.librarysystem.model.Author;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.Genre;
import com.library.librarysystem.repository.AuthorRepo;
import com.library.librarysystem.repository.BookRepo;
import com.library.librarysystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    BookRepo bookRepo;
    AuthorRepo authorRepo;

    @Autowired
    public BookServiceImpl(BookRepo bookRepo, AuthorRepo authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }


    @Override
    public Book addBook(Book newBook) {
        if (!bookRepo.findByIsbn(newBook.getIsbn()).isPresent()) {
            return bookRepo.save(newBook);
        }
        return null; // add throw statement
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public List<Book> getBooksByGenre(Genre genre) {
        return bookRepo.findAllByGenre(genre);
    }

    @Override
    public List<Book> getBooksByAuthor(Long id) {
        Optional<Author> author = authorRepo.findById(id);
        if (author.isPresent()){
            return bookRepo.findBookByAuthorsContains(author.get());
        }
        return null; // add throw statement
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepo.findAllByTitleContains(title);
    }

    @Override
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepo.findById(id);
        if (book.isPresent()){
            return book.get();
        }
        return null; // add throw statement
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<Book> book = bookRepo.findByIsbn(isbn);
        if (book.isPresent()){
            return book.get();
        }
        return null; // add throw statement
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookRepo.deleteById(bookId);
    }
}

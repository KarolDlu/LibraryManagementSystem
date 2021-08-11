package com.library.librarysystem.service.impl;

import com.library.librarysystem.exceptions.BookAlreadyExists;
import com.library.librarysystem.exceptions.ObjectAlreadyExistsException;
import com.library.librarysystem.exceptions.ObjectNotFoundException;
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
        return (Book) bookRepo.findByIsbn(newBook.getIsbn()).map(book -> {
            throw new BookAlreadyExists(newBook.getIsbn());
        }).orElse(bookRepo.save(newBook));
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
        Optional<Author> optAuthor = authorRepo.findById(id);
        return optAuthor.map(author -> bookRepo.findBookByAuthorsContains(author)).orElseThrow(() -> {
            throw new ObjectNotFoundException("Author", id);
        });
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepo.findAllByTitleContains(title);
    }

    @Override
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepo.findById(id);
        return book.orElseThrow(() -> {
            throw new ObjectNotFoundException("Book", id);
        });
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<Book> book = bookRepo.findByIsbn(isbn);
        return book.orElseThrow(() -> {
            throw new ObjectNotFoundException("Book", "isbn", isbn);
        });
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookRepo.deleteById(bookId);
    }

    @Override
    public Author addAuthor(Author author) {
        return (Author) authorRepo.findAuthorByName(author.getName()).map(author1 -> {
            throw new ObjectAlreadyExistsException("Author with given name: " + author.getName() + " already exists.");
        }).orElse(authorRepo.save(author));

    }

    @Override
    public void deleteAuthor(Long authorId) {
        authorRepo.findById(authorId).ifPresentOrElse(author -> authorRepo.delete(author), () -> {
            throw new ObjectNotFoundException("Author", authorId);
        });
    }
}

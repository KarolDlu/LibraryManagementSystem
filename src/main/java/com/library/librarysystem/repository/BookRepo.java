package com.library.librarysystem.repository;

import com.library.librarysystem.model.Author;
import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findAllByGenre(Genre genre);

    List<Book> findAllByTitleContains(String title);

    List<Book> findBookByAuthorsContains(Author author);
}

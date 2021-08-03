package com.library.librarysystem.service;

import com.library.librarysystem.model.BookLending;

public interface LibraryService {

    BookLending borrowBook(Long memberId, Long bookItemId);

    boolean returnBook(Long memberId, Long bookItemId);

    boolean renewBook(Long memberId, Long bookItemId);

}

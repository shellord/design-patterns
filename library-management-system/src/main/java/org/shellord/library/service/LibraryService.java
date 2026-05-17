package org.shellord.library.service;

import org.shellord.library.model.Book;
import org.shellord.library.model.LibraryResult;
import org.shellord.library.model.Member;


public interface LibraryService {
    public LibraryResult borrowBook(Member member, Book book);
    public LibraryResult returnBook(Member member, Book book);
}

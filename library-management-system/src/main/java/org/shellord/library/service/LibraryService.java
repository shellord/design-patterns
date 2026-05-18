package org.shellord.library.service;

import org.shellord.library.model.Book;
import org.shellord.library.model.LibraryResult;
import org.shellord.library.model.Member;
import org.shellord.library.model.MemberType;


public interface LibraryService {
    public LibraryResult borrowBook(String memberId, Book book);

    public LibraryResult returnBook(String memberId, Book book, MemberType memberType);
}

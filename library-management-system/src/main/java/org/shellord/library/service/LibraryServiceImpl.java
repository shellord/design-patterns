package org.shellord.library.service;

import org.shellord.library.model.Book;
import org.shellord.library.model.BorrowRecord;
import org.shellord.library.model.LibraryResult;
import org.shellord.library.model.Member;

import java.util.ArrayList;
import java.util.List;

public class LibraryServiceImpl implements LibraryService {

    private final List<BorrowRecord> borrowRecords = new ArrayList<>();

    @Override
    public LibraryResult borrowBook(Member member,Book book) {
        BorrowRecord borrowRecord = new BorrowRecord();

    }


    @Override
    public LibraryResult returnBook(Member member, Book book) {
        return null;
    }
}

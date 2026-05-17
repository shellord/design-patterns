package org.shellord.library;


import org.shellord.library.model.Book;
import org.shellord.library.model.Member;
import org.shellord.library.model.MemberType;
import org.shellord.library.service.LibraryService;
import org.shellord.library.service.LibraryServiceImpl;

public class Main {
    public static void main(String[] args) {
        LibraryService libraryService = new LibraryServiceImpl();

         Book book1 = new Book(
               "sdfjsal",
               "Oliver Twist",
               "Charles Dickens",
               "Novel",
                1997,
                10
        );

        Book book2 = new Book();
        book2.setTitle("Black Beauty");
        book2.setAuthor("Anna Sewell");
        book2.setIsbn("123456789");
        book2.setGenre("Novel");
        book2.setPublishedYear(1997);
        book2.setAvailableCopies(5);

        libraryService.borrowBook(new Member("1","Saheen","saheen@gmail.com", MemberType.STANDARD),book1);
        libraryService.borrowBook(new Member("1","Saheen","saheen@gmail.com", MemberType.STANDARD),book2);


    }
}

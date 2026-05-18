package org.shellord.library;


import org.shellord.library.model.Book;
import org.shellord.library.model.Member;
import org.shellord.library.model.MemberType;
import org.shellord.library.service.LibraryService;
import org.shellord.library.service.LibraryServiceImpl;

public class Main {
    public static void main(String[] args)  {
        LibraryService libraryServiceImpl = new LibraryServiceImpl();

        // constructor init
        Book book1 = new Book(
                "sdfjsal",
                "Oliver Twist",
                "Charles Dickens",
                "Novel",
                1997,
                10
        );

        // setters
        Book book2 = new Book();
        book2.setTitle("Black Beauty");
        book2.setAuthor("Anna Sewell");
        book2.setIsbn("123456789");
        book2.setGenre("Novel");
        book2.setPublishedYear(1997);
        book2.setAvailableCopies(5);

        // builder
        Member member1 = new Member.Builder()
                .id("dsfljf")
                .name("Saheen")
                .email("saheen@gmail.com")
                .type(MemberType.STANDARD)
                .build();

        libraryServiceImpl.borrowBook(
               member1.getId(),
               book1
        );

        libraryServiceImpl.returnBook(
                member1.getId(),
                book1,
                MemberType.PREMIUM
        );

    }
}

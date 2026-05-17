package org.shellord.library.model;

import java.time.LocalDate;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private int publishedYear;
    private int availableCopies;

    public Book(String isbn, String title, String author, String genre, int publishedYear, int availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishedYear = LocalDate.now().getYear();
        this.availableCopies = availableCopies;
    }

    public Book(){}

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}

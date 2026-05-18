package org.shellord.library.model;

import java.math.BigDecimal;

public class LibraryResult {
    private String memberId;
    private String isbn;
    private BigDecimal lateFee;
    private boolean overDue;
    private String message;

    public LibraryResult( String memberId, String isbn, BigDecimal lateFee, boolean overDue, String message) {
        this.memberId = memberId;
        this.isbn = isbn;
        this.lateFee = lateFee;
        this.overDue = overDue;
        this.message = message;
    }

   public LibraryResult(String memberId, String isbn, BigDecimal lateFee, boolean overDue) {
        this( memberId, isbn, lateFee, overDue, null);
   }

    public String getMemberId() {
        return memberId;
    }

    public String getIsbn() {
        return isbn;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public boolean isOverDue() {
        return overDue;
    }

    // builder
    public static class Builder {
        private String memberId;
        private String isbn;
        private BigDecimal lateFee;
        private boolean overDue;
        private String message;


        public Builder memberId(String memberId) {
            this.memberId = memberId;
            return this;
        }
        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }
        public Builder lateFee(BigDecimal lateFee) {
            this.lateFee = lateFee;
            return this;
        }
        public Builder overDue(boolean overDue) {
            this.overDue = overDue;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public LibraryResult build() {
            if (memberId == null || isbn == null || lateFee == null) {
                throw new IllegalArgumentException("Missing required arguments");
            }
            return new LibraryResult( memberId, isbn, lateFee, overDue, message);
        }
    }

    // static factory methods
    public static LibraryResult create(String memberId, String isbn, BigDecimal lateFee, boolean overDue) {
        return new LibraryResult(
                memberId,
                isbn,
                lateFee,
                overDue,
                null
        );
    }
    public static LibraryResult create(String memberId, String isbn, BigDecimal lateFee, boolean overDue, String message) {
        return new LibraryResult(
                memberId,
                isbn,
                lateFee,
                overDue,
                message
        );
    }
}

package org.shellord.library.service;

import org.shellord.library.fee.LateFeeStrategy;
import org.shellord.library.fee.LateFeeStrategyFactory;
import org.shellord.library.model.Book;
import org.shellord.library.model.BorrowRecord;
import org.shellord.library.model.LibraryResult;
import org.shellord.library.model.MemberType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryServiceImpl implements LibraryService {

    private final List<BorrowRecord> borrowRecords = new ArrayList<>();

    @Override
    public LibraryResult borrowBook(String memberId, Book book) {
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(7);

        BorrowRecord borrowRecord = new BorrowRecord(
                book.getIsbn(),
                memberId,
                borrowDate,
                dueDate,
                null
        );
        borrowRecords.add(borrowRecord);
        return new LibraryResult.Builder()
                .isbn(book.getIsbn())
                .memberId(memberId)
                .lateFee(new BigDecimal("0"))
                .overDue(false)
                .build();
    }


    @Override
    public LibraryResult returnBook(String memberId, Book book, MemberType memberType) {
        Optional<BorrowRecord> borrowRecord =  borrowRecords.stream()
                .filter(r -> r.getIsbn().equals(book.getIsbn()))
                .filter(r -> r.getMemberId().equals(memberId))
                .findFirst();
        if(borrowRecord.isEmpty()) {
            throw new IllegalArgumentException("No record found");
        }
        LateFeeStrategy strategy = LateFeeStrategyFactory.getStrategy(memberType);
        BigDecimal lateFee = strategy.calculateFee(borrowRecord.get().getDueDate(), LocalDate.now());
        boolean isOverDue = lateFee.compareTo(BigDecimal.ZERO) > 0;
        borrowRecord.get().setReturnDate(LocalDate.now());
        book.incrementCopy();
        return new LibraryResult.Builder()
                .isbn(book.getIsbn())
                .memberId(memberId)
                .lateFee(lateFee)
                .overDue(isOverDue)
                .build();
    }
}

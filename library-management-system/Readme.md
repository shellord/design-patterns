# Library Management System 
## Problem Statement

Build a library management system that handles book borrowing, returns, late fee calculation, and member notifications. The system must support different member types with different fee rules, and notify relevant parties when key events occur.

---

## Domain Models

```java
class Book {
    String isbn;
    String title;
    String author;
    String genre;
    int publishedYear;
    int availableCopies;
}

class Member {
    String id;
    String name;
    String email;
    MemberType type;
}

class BorrowRecord {
    String id;
    String memberId;
    String isbn;
    LocalDate borrowDate;
    LocalDate dueDate;
    LocalDate returnDate;   // null if not yet returned
}

class LibraryResult {
    String recordId;
    String memberId;
    String isbn;
    BigDecimal lateFee;     // 0 if returned on time
    boolean overdue;
    String message;
}

enum MemberType { STANDARD, PREMIUM, STUDENT }
enum LibraryEvent { BOOK_BORROWED, BOOK_RETURNED, BOOK_OVERDUE }
```

---

## Core Methods to Implement

```java
// Borrow a book
LibraryResult borrowBook(Member member, Book book);

// Return a book and calculate any late fee
LibraryResult returnBook(BorrowRecord record, LocalDate returnDate);

// Get all overdue records
List<BorrowRecord> getOverdueRecords();
```

---

## Business Rules

### Borrowing rules

- A book can only be borrowed if `availableCopies > 0`
- A member can have at most 3 books borrowed at a time
- Due date is always 14 days from borrow date

### Late fee rules

Fee is calculated per day overdue. Rate depends on member type:

| Member type | Fee per day overdue |
|---|---|
| `STANDARD` | £1.00 |
| `PREMIUM` | £0.50 (half price) |
| `STUDENT` | £0.25 (quarter price) |

```
daysOverdue = returnDate - dueDate   (0 if returned on time)
lateFee     = max(daysOverdue, 0) × dailyRate
```

### Overdue rule

A borrow record is overdue when:

- `returnDate` is null (not yet returned) AND `dueDate` is before today, OR
- `returnDate` is not null AND `returnDate` is after `dueDate`

---

## Worked Example

**Member:** Alice, type `STANDARD`

**Book:** "Clean Code", 3 copies available

**Borrow date:** 2024-01-01 → **due date:** 2024-01-15

**Return date:** 2024-01-20 (5 days late)

**Calculation:**
1. `daysOverdue = 5`
2. `STANDARD` rate = £1.00/day
3. `lateFee = 5 × 1.00 = £5.00`
4. `overdue = true`

**Expected result:**
```
LibraryResult(
  memberId  = "alice-1",
  isbn      = "978-0132350884",
  lateFee   = 5.00,
  overdue   = true,
  message   = "Book returned late. Fee: £5.00"
)
```

---

## Design Patterns to Apply

### Stage 1 — no patterns (start here)
Write `LibraryService` as a single class. Hardcode the fee logic with `if/else` on `MemberType`. Get it working first.

### Stage 2 — Strategy
Extract a `LateFeeStrategy` interface. Create `StandardLateFeeStrategy`, `PremiumLateFeeStrategy`, `StudentLateFeeStrategy`. Each implements:

```java
public interface LateFeeStrategy {
    BigDecimal calculate(BorrowRecord record, LocalDate returnDate);
}
```

### Stage 3 — Factory
Add `LateFeeStrategyFactory` that maps `MemberType` to the correct strategy. `LibraryService` calls the factory — it never touches `MemberType` directly again.

```java
public interface LateFeeStrategyFactory {
    LateFeeStrategy getStrategy(MemberType memberType);
}
```

### Stage 4 — Builder
`Book` and `LibraryResult` are getting many fields. Add builders so construction is readable:

```java
Book book = new BookBuilder()
    .isbn("978-0132350884")
    .title("Clean Code")
    .author("Robert C. Martin")
    .genre("Software Engineering")
    .publishedYear(2008)
    .availableCopies(3)
    .build();
```

### Stage 5 — Observer
When key events happen, notify registered listeners. Define:

```java
public interface LibraryEventListener {
    void onEvent(LibraryEvent event, BorrowRecord record);
}
```

Implement two listeners:
- `WaitlistNotifier` — when a book is returned, notify the next member waiting for it
- `OverdueNotifier` — when a book is overdue, notify the member by email

`LibraryService` holds a `List<LibraryEventListener>` and fires events. Adding a new notification requires zero changes to `LibraryService`.

---

## Suggested Class Structure

```
LibraryService
│
├── model/
│   ├── Book.java
│   ├── Member.java
│   ├── BorrowRecord.java
│   ├── LibraryResult.java
│   ├── MemberType.java
│   └── LibraryEvent.java
│
├── strategy/
│   ├── LateFeeStrategy.java
│   ├── StandardLateFeeStrategy.java
│   ├── PremiumLateFeeStrategy.java
│   └── StudentLateFeeStrategy.java
│
├── factory/
│   └── LateFeeStrategyFactory.java
│
├── builder/
│   ├── BookBuilder.java
│   └── LibraryResultBuilder.java
│
└── observer/
    ├── LibraryEventListener.java
    ├── WaitlistNotifier.java
    └── OverdueNotifier.java
```

---

## Unit Tests to Write

- `STANDARD` member returns book 5 days late → fee = £5.00
- `PREMIUM` member returns book 5 days late → fee = £2.50
- `STUDENT` member returns book 5 days late → fee = £1.25
- Book returned on time → fee = £0.00
- Borrow fails when `availableCopies = 0`
- Borrow fails when member already has 3 books
- `getOverdueRecords()` returns only unreturned records past due date
- Observer is called exactly once when a book is returned
- Observer is not called when borrow fails

---

## Progression Tips

Work through the stages in order — do not skip ahead:

1. Get `LibraryService` working with plain `if/else` — feel the problem first
2. Refactor fee logic into Strategy — notice how `LibraryService` shrinks
3. Add Factory — notice `LibraryService` no longer imports `MemberType` logic
4. Add Builder — notice how test setup becomes readable
5. Add Observer — notice how `LibraryService` never changes when you add a new notification

Each refactor should leave all existing tests green.
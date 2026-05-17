# Invoice Processing Engine 

## Problem Statement

Build an invoice processing engine for a SaaS billing platform. The system must support multiple invoice types, validate payments, apply business rules, and calculate outstanding balances.

---

## Core Method to Implement

```java
InvoiceResult processInvoice(Invoice invoice, List<Payment> payments);
```

This method must:

1. Apply invoice-type-specific calculation rules
2. Filter and sum only valid payments
3. Calculate the outstanding balance
4. Determine whether the invoice is overdue
5. Return a fully populated `InvoiceResult`

---

## Domain Models

```java
class Invoice {
    String id;
    String customerId;
    InvoiceType type;
    BigDecimal amount;
    BigDecimal discountPercentage; // DISCOUNTED invoices only
    int subscriptionMonths;        // SUBSCRIPTION invoices only
    LocalDate dueDate;
}

class Payment {
    String id;
    String invoiceId;
    PaymentMethod method;
    BigDecimal amount;
    LocalDate paymentDate;
}

class InvoiceResult {
    String invoiceId;
    String customerId;
    BigDecimal originalAmount;
    BigDecimal finalAmount;
    BigDecimal amountPaid;
    BigDecimal outstandingAmount;
    boolean overdue;
}

enum InvoiceType   { STANDARD, SUBSCRIPTION, DISCOUNTED, REFUND }
enum PaymentMethod { CARD, BANK_TRANSFER, WALLET }
```

---

## Invoice Type Rules

| Type | Rule | Formula |
|---|---|---|
| `STANDARD` | No special rules | `finalAmount = amount` |
| `SUBSCRIPTION` | 10% discount if `subscriptionMonths > 12` | `finalAmount = amount × 0.90` |
| `DISCOUNTED` | Apply invoice's `discountPercentage` | `finalAmount = amount × (1 - discountPercentage / 100)` |
| `REFUND` | Money owed back to customer | `finalAmount = -amount` |

---

## Payment Validation Rules

A payment is **valid** only when all of the following are true:

- `amount > 0`
- `paymentDate` is not in the future
- `invoiceId` matches the invoice being processed
- Payment `id` is not a duplicate (first occurrence wins)

---

## Balance & Overdue Calculation

```
outstandingAmount = max(finalAmount - totalPaid, 0)

overdue = outstandingAmount > 0 AND dueDate is before today
```

---

## Worked Example

**Invoice:**
```
id:                 INV-1
customerId:         CUST-1
type:               SUBSCRIPTION
amount:             1000
subscriptionMonths: 15
dueDate:            2025-01-01
```

**Payments:**
```
PAY-1: 200
PAY-2: 300
```

**Step-by-step:**
1. `subscriptionMonths = 15 > 12` → apply 10% discount → `finalAmount = 900`
2. Both payments valid → `totalPaid = 500`
3. `outstandingAmount = max(900 - 500, 0) = 400`
4. `dueDate` is in the past and `outstandingAmount > 0` → `overdue = true`

**Expected result:**
```
InvoiceResult(
  invoiceId         = "INV-1",
  customerId        = "CUST-1",
  originalAmount    = 1000,
  finalAmount       = 900,
  amountPaid        = 500,
  outstandingAmount = 400,
  overdue           = true
)
```

---

## Suggested Class Structure

Design your solution to be open for extension, closed for modification. New invoice types should require zero changes to existing classes.

```
InvoiceProcessingService
InvoiceCalculationStrategy      ← interface
  StandardInvoiceStrategy
  SubscriptionInvoiceStrategy
  DiscountedInvoiceStrategy
  RefundInvoiceStrategy
InvoiceStrategyFactory
PaymentValidator
InvoiceResult (+ Builder)
```

### Design patterns to apply

- **Strategy** — one class per invoice type, each implementing `InvoiceCalculationStrategy`
- **Factory** — `InvoiceStrategyFactory.getStrategy(InvoiceType)` returns the correct strategy
- **Builder** — construct `InvoiceResult` step by step without a telescoping constructor
- **Validator** — `PaymentValidator` encapsulates all payment filtering rules

---

## Unit Tests to Write

Cover at least the following cases:

- Each invoice type calculates `finalAmount` correctly
- `SUBSCRIPTION` with `subscriptionMonths <= 12` gets no discount
- Duplicate payment IDs — only first is counted
- Payment with `amount <= 0` is rejected
- Payment with future `paymentDate` is rejected
- Payment with wrong `invoiceId` is excluded
- Overpayment — `outstandingAmount` floors at 0, never goes negative
- Overdue = true when `outstandingAmount > 0` and `dueDate` is past
- Overdue = false when fully paid, even if `dueDate` is past

---

## Bonus

```java
Map<String, BigDecimal> calculateOutstandingByCustomer(
    List<Invoice> invoices,
    List<Payment> payments
);
```

Return total outstanding balance grouped by `customerId`. Implement using the Streams API.

**Further extensions (optional):**
- Top N customers by outstanding balance
- Batch processing with parallel streams
- Multi-currency support with an exchange rate service
- Export results as CSV or JSON

---

## Things to discuss during pairing

- Where exactly do the rounding rules go — inside the strategy, or in a shared utility? Use `BigDecimal.setScale(2, RoundingMode.HALF_UP)` consistently.
- Should `InvoiceProcessingService` throw checked exceptions or return a result type when processing fails?
- How would you add a new invoice type (e.g. `PRORATED`) without touching any existing class?
- Would you make `InvoiceCalculationStrategy` a `@FunctionalInterface`? What do you gain or lose?
- How does `PaymentValidator` get injected — and how does that affect testability?
package com.manerajona.core.domain;

import java.util.UUID;

public record Loan(
        UUID guid,
        double requestedAmount,
        int termMonths,
        double annualInterest,
        Borrower borrower
) {
}
package com.manerajona.core.domain;

public record Borrower(
        String fullName,
        int age,
        double annualIncome,
        double annualDebt,
        boolean delinquentDebt
) {
}
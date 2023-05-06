package com.manerajona.ports.output.db;

import com.manerajona.core.domain.Loan;
import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Contract
public interface LoanDao {

    Optional<Loan> findById(UUID id) throws SQLException;

    UUID save(Loan entity) throws SQLException;

    void updateAmountTermMonthsAndInterest(UUID guid, double amount, int termMonths, double annualInterest)
            throws SQLException;
}
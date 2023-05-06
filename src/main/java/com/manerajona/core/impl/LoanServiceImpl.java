package com.manerajona.core.impl;

import com.manerajona.common.exceptions.ResourceNotFoundException;
import com.manerajona.core.LoanService;
import com.manerajona.core.domain.Loan;
import com.manerajona.ports.output.db.LoanDao;
import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.sql.SQLException;
import java.util.UUID;

@Service
final class LoanServiceImpl implements LoanService {

    @Inject
    private LoanDao dao;

    @Override
    public UUID createLoan(Loan loan) {
        try {
            return dao.save(loan);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public Loan findLoan(UUID guid) throws ResourceNotFoundException {
        try {
            return dao.findById(guid).orElseThrow(ResourceNotFoundException::new);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public boolean renegotiateLoan(UUID guid, Loan loan) {
        boolean renegociated = true;
        try {
            dao.updateAmountTermMonthsAndInterest(guid, loan.requestedAmount(), loan.termMonths(),
                    loan.annualInterest());
        } catch (SQLException ignored) {
            renegociated = false;
        }
        return renegociated;
    }
}
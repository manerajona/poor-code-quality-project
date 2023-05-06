package com.manerajona.core;

import com.manerajona.core.domain.Loan;
import org.jvnet.hk2.annotations.Contract;

import java.util.UUID;

@Contract
public interface LoanService {

    UUID createLoan(Loan loan);

    Loan findLoan(UUID guid);

    boolean renegotiateLoan(UUID guid, Loan loan);
}

package com.manerajona.ports.output.db.impl;

import com.manerajona.common.exceptions.SubResourceNotFoundException;
import com.manerajona.core.domain.Borrower;
import com.manerajona.core.domain.Loan;
import com.manerajona.ports.output.db.BorrowerDao;
import com.manerajona.ports.output.db.LoanDao;
import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
final class LoanDaoImpl implements LoanDao {

    private static final String INSERT_SQL = """
            insert into loan (guid, amount, term_months, interest, borrower_guid)
            values (?, ?, ?, ?, ?)
            """;
    private static final String SELECT_SQL = "select * from loan where guid = ?";

    private static final String UPDATE_AMOUNT_TERM_MONTHS_AND_INTEREST_SQL = """
            update loan
            set amount = ?, term_months = ?, interest = ?
            where guid = ?
            """;

    @Inject
    private ConnectionManager database;

    @Inject
    private BorrowerDao borrowerDao;

    @Override
    public Optional<Loan> findById(UUID id) throws SQLException, SubResourceNotFoundException {
        Loan loan = null;
        try (PreparedStatement statement = database.getConnection().prepareStatement(SELECT_SQL)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                UUID borrowerId = UUID.fromString(resultSet.getString("borrower_guid"));
                Borrower borrower = borrowerDao.findById(borrowerId)
                        .orElseThrow(SubResourceNotFoundException::new);

                loan = new Loan(
                        UUID.fromString(resultSet.getString("guid")),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("term_months"),
                        resultSet.getLong("interest"),
                        borrower
                );
            }
        } catch (SQLException e) {
            System.out.println("There was an error finding loan, guid=" + id);
            e.printStackTrace();
            throw new SQLException(e);
        }
        return Optional.ofNullable(loan);
    }

    @Override
    public UUID save(Loan loan) throws SQLException {
        UUID guid;
        try (PreparedStatement statement = database.getConnection().prepareStatement(INSERT_SQL)) {

            guid = UUID.randomUUID();
            statement.setString(1, guid.toString());
            statement.setDouble(2, loan.requestedAmount());
            statement.setInt(3, loan.termMonths());
            statement.setDouble(4, loan.annualInterest());

            UUID borrowerGuid = borrowerDao.save(loan.borrower());
            statement.setString(5, borrowerGuid.toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("There was an error saving entity," + loan);
            e.printStackTrace();
            throw new SQLException(e);
        }
        return guid;
    }

    @Override
    public void updateAmountTermMonthsAndInterest(UUID guid, double amount, int termMonths, double annualInterest)
            throws SQLException {
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement(UPDATE_AMOUNT_TERM_MONTHS_AND_INTEREST_SQL);
            statement.setDouble(1, amount);
            statement.setInt(2, termMonths);
            statement.setDouble(3, annualInterest);
            statement.setString(4, guid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("There was an error updating loan, guid=" + guid);
            e.printStackTrace();
            throw new SQLException(e);
        }
    }
}
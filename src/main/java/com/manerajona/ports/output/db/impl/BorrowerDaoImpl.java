package com.manerajona.ports.output.db.impl;

import com.manerajona.core.domain.Borrower;
import com.manerajona.ports.output.db.BorrowerDao;
import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
final class BorrowerDaoImpl implements BorrowerDao {

    private static final String INSERT_SQL = """
            insert into borrower (guid, name, age, income, debt, delinquent)
            values (?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_SQL = "select * from borrower where guid = ?";

    @Inject
    private ConnectionManager database;

    @Override
    public Optional<Borrower> findById(UUID id) throws SQLException {
        Borrower borrower = null;
        try (PreparedStatement statement = database.getConnection().prepareStatement(SELECT_SQL)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                borrower = new Borrower(
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getDouble("income"),
                        resultSet.getDouble("debt"),
                        resultSet.getBoolean("delinquent")
                );
            }
        } catch (SQLException e) {
            System.out.println("There was an error finding borrower, guid=" + id);
            e.printStackTrace();
            throw new SQLException(e);
        }
        return Optional.ofNullable(borrower);
    }

    @Override
    public UUID save(Borrower borrower) throws SQLException {
        UUID guid;
        try (PreparedStatement statement = database.getConnection().prepareStatement(INSERT_SQL)) {

            guid = UUID.randomUUID();
            statement.setString(1, guid.toString());
            statement.setString(2, borrower.fullName());
            statement.setInt(3, borrower.age());
            statement.setDouble(4, borrower.annualIncome());
            statement.setDouble(5, borrower.annualDebt());
            statement.setBoolean(6, borrower.delinquentDebt());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("There was an error saving entity, " + borrower);
            e.printStackTrace();
            throw new SQLException(e);
        }
        return guid;
    }

}
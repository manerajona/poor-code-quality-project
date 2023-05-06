package com.manerajona.ports.output.db;

import com.manerajona.core.domain.Borrower;
import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Contract
public interface BorrowerDao {

    Optional<Borrower> findById(UUID id) throws SQLException;

    UUID save(Borrower entity) throws SQLException;
}
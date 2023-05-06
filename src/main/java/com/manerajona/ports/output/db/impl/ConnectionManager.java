package com.manerajona.ports.output.db.impl;

import com.manerajona.common.constants.DatabaseConstants;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Contract
@Service
final class ConnectionManager {

    private final Connection connection;

    ConnectionManager() throws SQLException {
        this.connection = DriverManager.getConnection(
                DatabaseConstants.URL, "root", "password"
        );
    }

    Connection getConnection() {
        return connection;
    }

}
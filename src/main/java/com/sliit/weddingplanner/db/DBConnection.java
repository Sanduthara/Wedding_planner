package com.sliit.weddingplanner.db;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBConnection {
    private final DataSource dataSource;

    @Autowired
    public DBConnection(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

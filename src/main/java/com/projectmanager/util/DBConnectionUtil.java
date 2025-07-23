package com.projectmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/project_manager";
    private static final String USERNAME = "root";
    private static Connection connection = null;

    public static Connection getConnection(String password) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, password);
        }
        return connection;
    }
}

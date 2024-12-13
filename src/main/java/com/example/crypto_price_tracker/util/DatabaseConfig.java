package com.example.crypto_price_tracker.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConfig {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/coinflipperdatabase";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "iftheycanwhycantyou$65";
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            throw e;
        }
    }
}

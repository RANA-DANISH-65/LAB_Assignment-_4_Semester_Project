package com.example.crypto_price_tracker.util;

import com.example.crypto_price_tracker.models.TradeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PortfolioOperationHandler {
    public static ArrayList<TradeRecord> tradeRecords = new ArrayList<>();
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `%s` (recordID INT AUTO_INCREMENT PRIMARY KEY, coinName VARCHAR(255), tradeDate VARCHAR(255), tradeType VARCHAR(255), quantity DOUBLE, buyPrice DOUBLE, sellPrice DOUBLE, totalValue DOUBLE, profitOrLoss DOUBLE);";
    private static final String INSERT_RECORD_QUERY = "INSERT INTO `%s` (coinName, tradeDate, tradeType, quantity, buyPrice, sellPrice, totalValue, profitOrLoss) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_RECORD_QUERY = "DELETE FROM `%s` WHERE recordID = ?;";

    public static void createTable(String userEmail) {
        String tableName = sanitizeEmailToTableName(userEmail);
        try (Connection connection = PortfolioDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(CREATE_TABLE_QUERY, tableName))) {
            statement.executeUpdate();
            System.out.println("Table created or already exists for user: " + userEmail);
        } catch (SQLException e) {
            System.err.println("Error creating table for user " + userEmail + ": " + e.getMessage());
        }
    }

    public static void addRecord(String userEmail, String coinName, String tradeDate, String tradeType, double quantity, double buyPrice, double sellPrice, double totalValue, double profitOrLoss) {
        String tableName = sanitizeEmailToTableName(userEmail);
        try (Connection connection = PortfolioDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(INSERT_RECORD_QUERY, tableName))) {
            statement.setString(1, coinName);
            statement.setString(2, tradeDate);
            statement.setString(3, tradeType);
            statement.setDouble(4, quantity);
            statement.setDouble(5, buyPrice);
            statement.setDouble(6, sellPrice);
            statement.setDouble(7, totalValue);
            statement.setDouble(8, profitOrLoss);
            statement.executeUpdate();
            System.out.println("Record added to table for user: " + userEmail);

        } catch (SQLException e) {
            System.err.println("Error adding record for user " + userEmail + ": " + e.getMessage());
        }
    }

    public static void updateRecord(
            String userEmail,
            int recordID,
            String coinName,
            String tradeDate,
            String tradeType,
            double quantity,
            double buyPrice,
            double sellPrice,
            double totalValue,
            double profitOrLoss) {
        String tableName = sanitizeEmailToTableName(userEmail);

        String updateQuery = String.format(
                "UPDATE %s SET coinName = ?, tradeDate = ?, tradeType = ?, quantity = ?, buyPrice = ?, sellPrice = ?, totalValue = ?, profitOrLoss = ? WHERE recordID = ?",
                tableName);

        try (
                Connection connection = PortfolioDataBaseConfig.getConnection();
                PreparedStatement statement = connection.prepareStatement(updateQuery)
        ) {
            statement.setString(1, coinName);
            statement.setString(2, tradeDate);
            statement.setString(3, tradeType);
            statement.setDouble(4, quantity);
            statement.setDouble(5, buyPrice);
            statement.setDouble(6, sellPrice);
            statement.setDouble(7, totalValue);
            statement.setDouble(8, profitOrLoss);
            statement.setInt(9, recordID);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record updated successfully for user: " + userEmail);
            } else {
                System.out.println("No record found to update for user: " + userEmail);
            }

        } catch (SQLException e) {
            System.err.println("Error updating record for user " + userEmail + ": " + e.getMessage());
        }
    }


    public static void deleteRecord(String userEmail, int recordID) {
        String tableName = sanitizeEmailToTableName(userEmail);
        try (Connection connection = PortfolioDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(DELETE_RECORD_QUERY, tableName))) {
            System.out.println(recordID);
            statement.setInt(1, recordID);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record deleted from table for user: " + userEmail);
            } else {
                System.out.println("No record found to delete for user: " + userEmail);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting record for user " + userEmail + ": " + e.getMessage());
        }
    }

    private static String sanitizeEmailToTableName(String email) {
        return email.replace("@", "_at_").replace(".", "_dot_");
    }

    public static void loadRecordsFromDatabase(String userEmail) {
        String tableName = sanitizeEmailToTableName(userEmail);
        tradeRecords.clear();
        String query = "SELECT * FROM `" + tableName + "`";

        try (Connection connection = PortfolioDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int recordId = resultSet.getInt("recordID");
                String coinName = resultSet.getString("coinName");
                String tradeDate = resultSet.getString("tradeDate");
                String tradeType = resultSet.getString("tradeType");
                double quantity = resultSet.getDouble("quantity");
                double buyPrice = resultSet.getDouble("buyPrice");
                double sellPrice = resultSet.getDouble("sellPrice");
                double totalValue = resultSet.getDouble("totalValue");
                double profitOrLoss = resultSet.getDouble("profitOrLoss");
                TradeRecord record = new TradeRecord(LocalDate.parse(tradeDate), coinName, tradeType, quantity, buyPrice, sellPrice, totalValue, profitOrLoss,recordId);
                tradeRecords.add(record);
            }

            System.out.println("Records loaded for user: " + userEmail);
            for (TradeRecord record : tradeRecords){
                System.out.println(record);
            }

        } catch (SQLException e) {
            System.err.println("Error loading records for user " + userEmail + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example user email
        String userEmail = "danish@gmail.com";

        // Test create table
//        PortfolioOperationHandler.createTable(userEmail);

        String coinName = "Bitcoin";
        String tradeDate = "2024-12-09";
        String tradeType = "BUY";
        double quantity = 10.5;
        double buyPrice = 0.25;
        double sellPrice = 0.0; // No sell price for buy transaction
        double totalValue = quantity * buyPrice;
        double profitOrLoss = 0.0; // No profit or loss for buy transaction

        PortfolioOperationHandler.updateRecord(userEmail,4, coinName, tradeDate, tradeType, quantity, buyPrice, sellPrice, totalValue, profitOrLoss);

        // Test updating a record

    }
}

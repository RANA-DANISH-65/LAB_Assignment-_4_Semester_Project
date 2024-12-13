package com.example.crypto_price_tracker.util;

import com.example.crypto_price_tracker.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static User currectUser;

    public static boolean addUser(String username, String email, String password, String passwordKey) {
        if (userExists(email)) {
            System.out.println("User already exists with the provided email!");
            return false;
        }
        String query = "INSERT INTO users (username, useremail,userpassword, passwordKey) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, passwordKey);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                PortfolioOperationHandler.createTable(email);
                PortfolioOperationHandler.loadRecordsFromDatabase(email);
                currectUser=getUserByEmail(email);
                System.out.println("User added successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
        return false;
    }


    public static User getCurrentUser(){
         return currectUser;
    }
    private static boolean userExists(String email) {
        String query = "SELECT * FROM users WHERE useremail = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
        }
        return false;
    }

    private static User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE useremail = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("useremail"),
                        resultSet.getString("userpassword"),
                        resultSet.getString("passwordKey")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by email: " + e.getMessage());
        }
        return null;
    }

    public static boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE useremail = ? AND userpassword = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                currectUser = getUserByEmail(email);
                assert currectUser != null;
                PortfolioOperationHandler.loadRecordsFromDatabase(currectUser.getUserEmail());
                System.out.println("User authenticated successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
        }
        return false;
    }

    public static String getPasswordByEmail(String email, String passwordKey) {
        String query = "SELECT userpassword FROM users WHERE useremail = ? AND passwordKey = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, passwordKey);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("userpassword");
            } else {
                System.out.println("No user found with this email or password recovery key!");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching password: " + e.getMessage());
        }
        return null;
    }

    public static boolean changePassword(String email, String oldPassword, String newPassword) {
        String queryCheck = "SELECT * FROM users WHERE useremail = ? AND userpassword = ?";
        String queryUpdate = "UPDATE users SET userpassword = ? WHERE useremail = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(queryCheck)) {
            checkStatement.setString(1, email);
            checkStatement.setString(2, oldPassword);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                try (PreparedStatement updateStatement = connection.prepareStatement(queryUpdate)) {
                    updateStatement.setString(1, newPassword);
                    updateStatement.setString(2, email);
                    int rowsUpdated = updateStatement.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Password changed successfully!");
                        return true;
                    } else {
                        System.out.println("Error updating password!");
                    }
                }
            } else {
                System.out.println("Old password is incorrect!");
            }
        } catch (SQLException e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
        return false;
    }
}

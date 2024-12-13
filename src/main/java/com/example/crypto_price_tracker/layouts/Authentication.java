package com.example.crypto_price_tracker.layouts;

import com.example.crypto_price_tracker.util.CUSTOMALERT;
import com.example.crypto_price_tracker.util.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Authentication {
    public static boolean isAuthenticated = false;
    private boolean isSignUpView = true;

    public void loadSignUpLayout() {
        Stage stage = new Stage();
        stage.setTitle("Sign Up / Login");
        Label signUpLabel = new Label("Sign Up");
        signUpLabel.setId("signUp");
        signUpLabel.setPadding(new Insets(0,0,0,80));
        Label alreadyHaveAccountLabel = new Label("Already have an account? Login");
        alreadyHaveAccountLabel.setStyle("-fx-text-fill: #cecece;-fx-font-size: 20px;");
        alreadyHaveAccountLabel.setOnMouseClicked(event -> switchToLoginView(stage));
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(50);
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefHeight(50);
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(50);
        Label passwordKeyLabel = new Label("Password Key:");
        PasswordField passwordKeyField = new PasswordField();
        passwordKeyField.setPromptText("Password Key (In case you forget your password)");
        passwordKeyField.setPrefHeight(50);
        Label passwordRecoveryInfoLabel = new Label("In case you forget your password, you can recover it using the Password Key.");
        passwordRecoveryInfoLabel.setId("passwordKey");
        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(100);
        signUpButton.setOnAction(event -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String passwordKey = passwordKeyField.getText();

            if (username == null || username.trim().isEmpty()) {
                CUSTOMALERT.showAlert("Invalid Input", "Username cannot be empty.", Alert.AlertType.ERROR);
                return;
            }
            if (email == null || email.trim().isEmpty()) {
                CUSTOMALERT.showAlert("Invalid Input", "Email cannot be empty.", Alert.AlertType.ERROR);
                return;
            }
            if (password == null || password.trim().isEmpty()) {
                CUSTOMALERT.showAlert("Invalid Input", "Password cannot be empty.", Alert.AlertType.ERROR);
                return;
            }
            if (passwordKey == null || passwordKey.trim().isEmpty()) {
                CUSTOMALERT.showAlert("Invalid Input", "Password Key cannot be empty.", Alert.AlertType.ERROR);
                return;
            }

            if (!email.endsWith("@gmail.com")) {
                CUSTOMALERT.showAlert("Invalid Email", "Please enter a valid Gmail address (example: example@gmail.com)", Alert.AlertType.ERROR);
                return;
            }
            isAuthenticated = UserDAO.addUser(username, email, password, passwordKey);
            if (isAuthenticated) {
                CUSTOMALERT.showAlert("Success Signing Up", "Account Created Successfully", Alert.AlertType.INFORMATION);
                homeLayout.loadNavbar();
                homeLayout.updateStage();
                stage.close();
            } else {
                CUSTOMALERT.showAlert("Error Signing Up", "User already exists. Please try again", Alert.AlertType.ERROR);
            }
        });
        alreadyHaveAccountLabel.setId("alreadyHaveAccountLabel");
        VBox vbox=new VBox(20,signUpButton,alreadyHaveAccountLabel);
        signUpButton.setAlignment(Pos.CENTER);
        alreadyHaveAccountLabel.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        GridPane.setColumnSpan(signUpLabel, 2);
        gridPane.add(signUpLabel, 0, 0, 2, 1);

        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(emailLabel, 0, 2);
        gridPane.add(emailField, 1, 2);
        gridPane.add(passwordLabel, 0, 3);
        gridPane.add(passwordField, 1, 3);
        gridPane.add(passwordKeyLabel, 0, 4);
        gridPane.add(passwordKeyField, 1, 4);
        gridPane.add(passwordRecoveryInfoLabel, 0, 5,2,1);
        gridPane.add(vbox, 1, 6);
        Scene signUpScene = new Scene(gridPane, 600, 500);
        signUpScene.getStylesheets().add(getClass().getResource("/styles/auth.css").toExternalForm());
        stage.setScene(signUpScene);
        stage.show();
    }



    private void switchToLoginView(Stage stage) {
        isSignUpView = false;
        Label titleLabel = new Label("Login");
        titleLabel.setId("login");
        Label signUpLabel = new Label("Don't Have an Account? Sign Up");
        signUpLabel.setId("signupLabel");
        signUpLabel.setOnMouseClicked(event -> switchToSignUpView(stage));
        Label forgotPasswordLabel = new Label("Forgot Password?");
        forgotPasswordLabel.setId("forgotPasswordLabel");
        forgotPasswordLabel.setOnMouseClicked(event -> loadForgotPasswordLayout(stage));
        Label usernameLabel = new Label("Email:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your email address ");
        usernameField.setPrefHeight(50);
        usernameField.setPrefWidth(300);
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(50);
        passwordField.setPrefWidth(300);
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(150);
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Username and Password cannot be empty.", Alert.AlertType.ERROR);
                return;
            }
            isAuthenticated = UserDAO.authenticateUser(username, password);
            if (isAuthenticated) {
                CUSTOMALERT.showAlert("Success Login", "Login Successful", Alert.AlertType.INFORMATION);
                homeLayout.loadNavbar();
                homeLayout.updateStage();
                stage.close();
            } else {
                CUSTOMALERT.showAlert("Error Login", "Invalid Credentials. Please try again", Alert.AlertType.ERROR);
            }
        });
        HBox usernameHBox = new HBox(10, usernameLabel, usernameField);
        usernameHBox.setAlignment(Pos.CENTER);
        HBox passwordHBox = new HBox(10, passwordLabel, passwordField);
        passwordHBox.setAlignment(Pos.CENTER);
        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(titleLabel, usernameHBox, passwordHBox, loginButton, signUpLabel, forgotPasswordLabel);
        Scene loginScene = new Scene(loginLayout, 400, 400);
        loginScene.getStylesheets().add(getClass().getResource("/styles/auth.css").toExternalForm());
        stage.setScene(loginScene);
    }


    private void switchToSignUpView(Stage stage) {
        isSignUpView = true;
        loadSignUpLayout();
    }

    private void loadForgotPasswordLayout(Stage stage) {
        Label titleLabel = new Label("Get Password");
        titleLabel.setId("getpassword");
        Label emailLabel = new Label("Email:");
        emailLabel.setPadding(new Insets(0,20,0,0));
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefHeight(50);
        emailField.setPrefWidth(300);
        Label passwordKeyLabel = new Label("Password Key:");
        PasswordField passwordKeyField = new PasswordField();
        passwordKeyField.setPromptText("Password Key");
        passwordKeyField.setPrefHeight(50);
        passwordKeyField.setPrefWidth(300);
        Button getPasswordButton = new Button("Get Password");
        getPasswordButton.setPrefWidth(150);
        getPasswordButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String passwordKey = passwordKeyField.getText().trim();
            if (email.isEmpty() || passwordKey.isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Email and Password Key cannot be empty.", Alert.AlertType.ERROR);
                return;
            }

            String password = UserDAO.getPasswordByEmail(email, passwordKey);
            if (password != null) {
                CUSTOMALERT.showAlert("Password", "Your Password is " + password, Alert.AlertType.INFORMATION);
                switchToLoginView(stage);
            } else {
                CUSTOMALERT.showAlert("Error Reset Password", "Invalid Email or Password Key. Please try again", Alert.AlertType.ERROR);
            }
        });

        HBox emailHBox = new HBox(50, emailLabel, emailField);
        emailHBox.setAlignment(Pos.CENTER);

        HBox passwordKeyHBox = new HBox(10, passwordKeyLabel, passwordKeyField);
        passwordKeyHBox.setAlignment(Pos.CENTER);


        VBox forgotPasswordLayout = new VBox(15);
        forgotPasswordLayout.setAlignment(Pos.CENTER);
        forgotPasswordLayout.getChildren().addAll(titleLabel, emailHBox, passwordKeyHBox, getPasswordButton);

        Scene forgotPasswordScene = new Scene(forgotPasswordLayout, 500, 400);
        forgotPasswordScene.getStylesheets().add(getClass().getResource("/styles/auth.css").toExternalForm());
        stage.setScene(forgotPasswordScene);
    }


    public void loadChangePasswordLayout() {
        Stage stage = new Stage();

        Label titleLabel = new Label("Change Password");
        titleLabel.setId("changePasswordTitle");

        Label oldPasswordLabel = new Label("Old Password:");
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPrefWidth(300);
        oldPasswordField.setPromptText("Old Password");
        oldPasswordField.setPrefHeight(50);


        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setPrefWidth(300);
        emailField.setPrefHeight(50);

        Label newPasswordLabel = new Label("New Password:");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPrefWidth(300);
        newPasswordField.setPromptText("New Password");
        newPasswordField.setPrefHeight(50);


        Label confirmNewPasswordLabel = new Label("Confirm New Password:");
        PasswordField confirmNewPasswordField = new PasswordField();
        confirmNewPasswordField.setPrefWidth(300);
        confirmNewPasswordField.setPromptText("Confirm New Password");
        confirmNewPasswordField.setPrefHeight(50);

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setPrefWidth(200);
        changePasswordButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String oldPassword = oldPasswordField.getText().trim();
            String newPassword = newPasswordField.getText().trim();
            String confirmNewPassword = confirmNewPasswordField.getText().trim();

            if (email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                CUSTOMALERT.showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR);
                return;
            }

            if (newPassword.equals(confirmNewPassword)) {
                boolean isPasswordChanged = UserDAO.changePassword(email, oldPassword, newPassword);
                if (isPasswordChanged) {
                    CUSTOMALERT.showAlert("Password Change Success", "Your password has been changed successfully", Alert.AlertType.INFORMATION);
                    switchToLoginView(stage);
                } else {
                    CUSTOMALERT.showAlert("Error Changing Password", "Old password is incorrect. Please try again", Alert.AlertType.ERROR);
                }
            } else {
                CUSTOMALERT.showAlert("Password Mismatch", "New passwords do not match. Please try again", Alert.AlertType.ERROR);
            }
        });


        HBox oldPasswordHBox = new HBox(10, oldPasswordLabel, oldPasswordField);
        oldPasswordHBox.setAlignment(Pos.CENTER);


        HBox emailHBox = new HBox(50, emailLabel, emailField);
        emailHBox.setAlignment(Pos.CENTER);


        HBox newPasswordHBox = new HBox(10, newPasswordLabel, newPasswordField);
        newPasswordHBox.setAlignment(Pos.CENTER);


        HBox confirmNewPasswordHBox = new HBox(10, confirmNewPasswordLabel, confirmNewPasswordField);
        confirmNewPasswordHBox.setAlignment(Pos.CENTER);

        VBox changePasswordLayout = new VBox(15);
        changePasswordLayout.setAlignment(Pos.CENTER);
        changePasswordLayout.getChildren().addAll(titleLabel, emailHBox, oldPasswordHBox, newPasswordHBox, confirmNewPasswordHBox, changePasswordButton);

        Scene changePasswordScene = new Scene(changePasswordLayout, 500, 400);
        changePasswordScene.getStylesheets().add(getClass().getResource("/styles/auth.css").toExternalForm());
        stage.setScene(changePasswordScene);
        stage.show();
    }

}

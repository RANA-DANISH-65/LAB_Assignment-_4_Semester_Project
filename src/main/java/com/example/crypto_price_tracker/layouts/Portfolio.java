package com.example.crypto_price_tracker.layouts;

import com.example.crypto_price_tracker.models.TradeManager;
import com.example.crypto_price_tracker.models.User;

import com.example.crypto_price_tracker.util.PortfolioOperationHandler;
import com.example.crypto_price_tracker.util.UserDAO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.stage.Stage;

public class Portfolio {
    private static Stage stage;
    public static void loadPortfolio() {
        if (stage == null) {
            stage = new Stage();
            stage.setFullScreen(true);
            stage.setTitle("Portfolio");
        }
        if (stage.isShowing()) {
            stage.close();
        }
        HBox mainLayout = new HBox();
        User currentUser = UserDAO.getCurrentUser();
        VBox sideBar = loadSideBar(currentUser);
        VBox portfolioSection = loadPortfolioSection();
        portfolioSection.setPadding(new Insets(20));
        sideBar.setMinWidth(250);
        portfolioSection.setMinWidth(1000);

        mainLayout.getChildren().clear();
        mainLayout.getChildren().addAll(sideBar, portfolioSection);

        Scene scene = new Scene(mainLayout,800,800);
        scene.getStylesheets().add(Portfolio.class.getResource("/styles/portfolio.css").toExternalForm());
        stage.setScene(scene);
        stage.setX(200);
        stage.setY(200);
        stage.setFullScreen(true);
        stage.show();
    }


    public static VBox loadSideBar(User user) {
        VBox userInfoSection = new VBox(10);
        userInfoSection.setPadding(new Insets(40,0,0,0));
        userInfoSection.setId("userInfo");
        userInfoSection.setAlignment(Pos.CENTER);
        Label userName = new Label(user.getUserName());
        userName.setAlignment(Pos.CENTER);
        Label userEmail = new Label(user.getUserEmail());
        userEmail.setAlignment(Pos.CENTER);
        userInfoSection.getChildren().addAll(userName, userEmail);

        VBox operationCenter = new VBox(10);
        operationCenter.setAlignment(Pos.CENTER);
        Button addRecordButton = new Button("Add Record");
        addRecordButton.setOnAction(e -> {
            PopUps.addRecordPopUp();
        });
        addRecordButton.setPrefWidth(150);
        addRecordButton.setAlignment(Pos.CENTER);
        Button updateRecordsButton = new Button("Update Records");
        updateRecordsButton.setOnAction(e -> {
            PopUps.getRecordPop();
        });
        updateRecordsButton.setPrefWidth(150);
        updateRecordsButton.setAlignment(Pos.CENTER);
        Button deleteRecordsButton = new Button("Delete Record");
        deleteRecordsButton.setOnAction(e -> {
            System.out.println("Button Clicked");
            PopUps.deleteRecordPopUp();
        });
        deleteRecordsButton.setPrefWidth(150);
        deleteRecordsButton.setAlignment(Pos.CENTER);
        Button changePasswordButton = new Button("Exit");
        changePasswordButton.setOnAction(e -> {
            stage.close();
        });
        changePasswordButton.setPrefWidth(150);
        changePasswordButton.setAlignment(Pos.CENTER);
        VBox summarySection = new VBox();
        String [] summaryInfo= TradeManager.getSummary();
        summarySection.setId("summary");
        summarySection.setAlignment(Pos.CENTER);
        Label part1 = new Label(summaryInfo[0]);
        Label part2 = new Label(summaryInfo[1]);
        Label part3 = new Label(summaryInfo[2]);
        Label part4 = new Label(summaryInfo[3]);
        Label part5 = new Label(summaryInfo[4]);
        Label part6 = new Label(summaryInfo[5]);
        Label part7= new Label(summaryInfo[6]);
        summarySection.getChildren().addAll(part1, part2, part3, part4, part5, part6, part7);
        operationCenter.getChildren().addAll(addRecordButton, updateRecordsButton, deleteRecordsButton, changePasswordButton);

        VBox sidebar = new VBox(80);
        sidebar.setId("Sidebar");
        sidebar.getChildren().addAll(userInfoSection, operationCenter,summarySection);
        return sidebar;


    }

    public static VBox loadPortfolioSection() {
        HBox headerSection = loadHeaderSection();
        VBox portfolioSection = new VBox(30);
        ScrollPane records = loadRecords();
        portfolioSection.getChildren().addAll(headerSection, records);
        portfolioSection.setPadding(new Insets(10));
        portfolioSection.setId("PortfolioSection");
        return portfolioSection;

    }

    public static HBox loadHeaderSection() {
        HBox headerBox = new HBox(100);
        headerBox.setPadding(new Insets(20));
        headerBox.setAlignment(Pos.CENTER);
        Label mainHeader = new Label("Stack Sats, Track Stats!");
        mainHeader.setId("mainHeader");
        mainHeader.setAlignment(Pos.CENTER);
        Label miniHeader = new Label("From moonshots to dips, let the coin flipper track your crypto trips.");
        miniHeader.setId("miniHeader");
        VBox headingSection = new VBox();
        headingSection.setAlignment(Pos.CENTER);
        headingSection.getChildren().addAll(mainHeader, miniHeader);



        headerBox.getChildren().addAll(headingSection);
        headerBox.setId("HeaderBox");
        return headerBox;

    }

    public static ScrollPane loadRecords() {
        FlowPane flowPane = new FlowPane();
        flowPane.setId("RecordsSection");
        flowPane.setPadding(new Insets(20));
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().clear();

        PortfolioOperationHandler.tradeRecords.forEach(record -> {
            VBox vbox = new VBox(10);
            vbox.setId("Record");
            Label tradeId = new Label("Trade ID: " + record.getRecordId());
            Label coinName = new Label("Coin Name: " + record.getCoinName());
            Label tradeDate = new Label("Date: " + record.getTradeDate());
            Label tradeQuantity = new Label("Quantity: " + record.getQuantity());
            Label tradePrice = new Label("Buy Price: $" + record.getBuyPrice());
            Label tradeTotal = new Label("Sell Price: $" + record.getSellPrice());
            Label tradeProfit = new Label("Profit/Loss: $" + record.getProfitOrLoss());
            Label tradeType = new Label("Type: " + record.getTradeType());
            Image image = new Image(Portfolio.class.getResource("/images/trade2.jpeg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            vbox.getChildren().addAll(
                    imageView,
                    tradeId, coinName, tradeDate, tradeQuantity,
                    tradePrice, tradeTotal, tradeProfit, tradeType
            );
            vbox.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 5;");
            flowPane.getChildren().add(vbox);
        });

        flowPane.setHgap(20);
        flowPane.setVgap(20);

        ScrollPane scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-padding: 10;");
        scrollPane.setId("RecordsSection");

        return scrollPane;
    }



}

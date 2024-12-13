package com.example.crypto_price_tracker.layouts;

import com.example.crypto_price_tracker.models.TradeManager;
import com.example.crypto_price_tracker.models.TradeRecord;
import com.example.crypto_price_tracker.util.CUSTOMALERT;
import com.example.crypto_price_tracker.util.PortfolioOperationHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PopUps {
    public static ArrayList<String> coins = new ArrayList<>(List.of(
            "Bitcoin", "Ethereum", "Litecoin", "Cardano", "Ripple", "Polkadot",
            "Dogecoin", "Solana", "Chainlink", "Binance Coin", "Shiba Inu", "Uniswap",
            "Avalanche", "Terra", "Litecoin", "Polygon", "VeChain", "Filecoin",
            "Cosmos", "Stellar", "Aave"
    ));

    public static void addRecordPopUp() {
        Stage popUpStage = new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        popUpStage.setTitle("Add Trade Record");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Trade Date");

        ComboBox<String> coinNameComboBox = new ComboBox<>();
        coinNameComboBox.getItems().addAll(coins);
        coinNameComboBox.setPromptText("Select Coin");

        ComboBox<String> tradeTypeComboBox = new ComboBox<>();
        tradeTypeComboBox.getItems().addAll("Buy", "Sell");
        tradeTypeComboBox.setPromptText("Select Trade Type");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");

        TextField priceField = new TextField();
        priceField.setPromptText("Enter Price per Coin (in $)");

        Label sellPriceLabel = new Label("Sell Price:");
        sellPriceLabel.setVisible(false);

        TextField sellPriceField = new TextField();
        sellPriceField.setPromptText("Enter Sell Price");
        sellPriceField.setVisible(false);
        Label quantityLabel = new Label("Quantity Bought:");
        Label priceLabel = new Label("Price per Coin (in $):");

        // Update form when trade type changes
        tradeTypeComboBox.setOnAction(e -> {
            if ("Sell".equals(tradeTypeComboBox.getValue())) {
                sellPriceLabel.setVisible(true);
                sellPriceField.setVisible(true);  // Show Sell Price field
                quantityLabel.setText("Quantity Sold:");  // Update to "Quantity Sold"
            } else {
                sellPriceField.setVisible(false);
                sellPriceField.setVisible(false);  // Hide Sell Price field
                quantityLabel.setText("Quantity Bought:");  // Update to "Quantity Bought"
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {

            if (datePicker.getValue() == null) {
                CUSTOMALERT.showAlert("Error", "Please select a valid trade date.", Alert.AlertType.ERROR);
                return;
            }
            if (coinNameComboBox.getValue() == null || coinNameComboBox.getValue().isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Please select a coin name.", Alert.AlertType.ERROR);
                return;
            }
            if (tradeTypeComboBox.getValue() == null || tradeTypeComboBox.getValue().isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Please select a trade type (Buy/Sell).", Alert.AlertType.ERROR);
                return;
            }
            if (quantityField.getText().isEmpty() || !quantityField.getText().matches("[0-9]*\\.?[0-9]+")) {
                CUSTOMALERT.showAlert("Error", "Please enter a valid quantity (numeric).", Alert.AlertType.ERROR);
                return;
            }
            if (priceField.getText().isEmpty() || !priceField.getText().matches("[0-9]*\\.?[0-9]+")) {
                CUSTOMALERT.showAlert("Error", "Please enter a valid price (numeric).", Alert.AlertType.ERROR);
                return;
            }
            if (datePicker.getValue().isAfter(LocalDate.now())) {
                CUSTOMALERT.showAlert("Error", "Trade date cannot be greater than today's date.", Alert.AlertType.ERROR);
                return;
            }

            LocalDate tradeDate = datePicker.getValue();
            String coinName = coinNameComboBox.getValue();
            String tradeType = tradeTypeComboBox.getValue();
            double quantity = Double.parseDouble(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            double sellPrice = 0;

            if ("Sell".equals(tradeType)) {
                if (sellPriceField.getText().isEmpty() || !sellPriceField.getText().matches("[0-9]*\\.?[0-9]+")) {
                    CUSTOMALERT.showAlert("Error", "Please enter a valid sell price (numeric).", Alert.AlertType.ERROR);
                    return;
                }
                sellPrice = Double.parseDouble(sellPriceField.getText());
            }
            if(sellPrice>0){
                TradeManager.addRecord(new TradeRecord(tradeDate,coinName,tradeType,quantity,price,sellPrice));
            }else{
                TradeManager.addRecord(new TradeRecord(tradeDate, coinName, tradeType, quantity, price));
            }
            CUSTOMALERT.showAlert("Record Added", "Your new record has been added successfully.", Alert.AlertType.INFORMATION);
            popUpStage.close();
            Portfolio.loadPortfolio();
        });

        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setOnAction(e -> popUpStage.close());

        // Layout for the form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(15));
        formGrid.add(new Label("Trade Date:"), 0, 0);
        formGrid.add(datePicker, 1, 0);
        formGrid.add(new Label("Coin Name:"), 0, 1);
        formGrid.add(coinNameComboBox, 1, 1);
        formGrid.add(new Label("Trade Type:"), 0, 2);
        formGrid.add(tradeTypeComboBox, 1, 2);
        formGrid.add(quantityLabel, 0, 3);
        formGrid.add(quantityField, 1, 3);
        formGrid.add(priceLabel, 0, 4);
        formGrid.add(priceField, 1, 4);
        formGrid.add(sellPriceLabel, 0, 5);
        formGrid.add(sellPriceField, 1, 5);

        // Button layout
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-alignment: center;");

        // Main layout
        VBox mainLayout = new VBox(10, formGrid, buttonBox);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        // Set the scene and show the pop-up
        Scene scene = new Scene(mainLayout, 400, 400);
        popUpStage.setScene(scene);
        popUpStage.showAndWait();
    }

    public static void deleteRecordPopUp() {
        System.out.println("Entered in Method");
        Stage stage = new Stage();


        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label label = new Label("Enter ID of Trade to Delete:");
        TextField textField = new TextField();
        textField.setPromptText("Enter ID of Trade to Delete");

        Button deleteButton = new Button("Delete");


        deleteButton.setOnAction(e -> {
            String input = textField.getText().trim();


            if (input.isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Please enter the ID of the trade to delete.", Alert.AlertType.ERROR);
                return;
            }

            try {
                int id = Integer.parseInt(input);

                if (id < 0) {
                    CUSTOMALERT.showAlert("Error", "ID cannot be negative.", Alert.AlertType.ERROR);
                    return;
                }


                boolean isDeleted = TradeManager.deleteRecord(id);

                if (isDeleted) {
                    CUSTOMALERT.showAlert("Success", "Trade deleted successfully.", Alert.AlertType.INFORMATION);
                    textField.clear();

                    stage.close();
                    Portfolio.loadPortfolio();
                } else {
                    CUSTOMALERT.showAlert("Error", "Trade with the given ID does not exist.", Alert.AlertType.ERROR);
                }

            } catch (NumberFormatException ex) {
                CUSTOMALERT.showAlert("Error", "Please enter a valid numeric ID.", Alert.AlertType.ERROR);
            }
        });


        vbox.getChildren().addAll(label, textField, deleteButton);


        Scene scene = new Scene(vbox, 300, 200);
        stage.setTitle("Delete Trade");
        stage.setScene(scene);

        System.out.println("Stage show");
        stage.show();
    }


    public static void getRecordPop() {
        Stage stage = new Stage();
        stage.setTitle("Retrieve Trade Record");


        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label label = new Label("Enter ID of Trade to Retrieve:");
        TextField textField = new TextField();
        textField.setPromptText("Enter ID of Trade to Retrieve");

        Button getButton = new Button("Get");
        getButton.setOnAction(e -> {
            String input = textField.getText().trim();


            if (input.isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Please enter the ID of the trade to retrieve.", Alert.AlertType.ERROR);
                return;
            }

            try {
                int recordId = Integer.parseInt(input);
                TradeRecord foundRecord = null;


                for (TradeRecord record : PortfolioOperationHandler.tradeRecords) {
                    if (record.getRecordId() == recordId) {
                        foundRecord = record;
                        break;
                    }
                }

                if (foundRecord != null) {
                    updateRecordPop(foundRecord);
                    stage.close();
                } else {
                    CUSTOMALERT.showAlert("Not Found", "No trade record found with ID: " + recordId, Alert.AlertType.WARNING);
                }

            } catch (NumberFormatException ex) {
                CUSTOMALERT.showAlert("Invalid Input", "Please enter a valid number for the trade ID.", Alert.AlertType.ERROR);
            }
        });


        vbox.getChildren().addAll(label, textField, getButton);

        Scene scene = new Scene(vbox, 400, 200);
        stage.setScene(scene);
        stage.show();
    }


    public static void updateRecordPop(TradeRecord record){
        Stage popUpStage = new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        popUpStage.setTitle("Add Trade Record");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(record.getTradeDate());
        datePicker.setPromptText("Select Trade Date");
        ComboBox<String> coinNameComboBox = new ComboBox<>();
        coinNameComboBox.getItems().addAll(coins);
        coinNameComboBox.setValue(record.getCoinName());
        coinNameComboBox.setPromptText("Select Coin");
        ComboBox<String> tradeTypeComboBox = new ComboBox<>();
        tradeTypeComboBox.getItems().addAll("Buy", "Sell");
        tradeTypeComboBox.setValue(record.getTradeType());
        tradeTypeComboBox.setPromptText("Select Trade Type");
        TextField quantityField = new TextField();
        quantityField.setText(String.valueOf(record.getQuantity()));
        quantityField.setPromptText("Enter Quantity");
        TextField priceField = new TextField();
        priceField.setText(String.valueOf(record.getBuyPrice()));
        priceField.setPromptText("Enter Price per Coin (in $)");
        Label sellPriceLabel = new Label("Sell Price:");
        sellPriceLabel.setVisible(true);
        TextField sellPriceField = new TextField();
        sellPriceField.setText(String.valueOf(record.getSellPrice()));
        sellPriceField.setPromptText("Enter Sell Price");
        sellPriceField.setVisible(false);
        if(!record.getTradeType().equalsIgnoreCase("Buy")){
            sellPriceLabel.setVisible(true);
            sellPriceField.setVisible(true);
        }
        Label quantityLabel = new Label("Quantity Bought:");
        Label priceLabel = new Label("Price per Coin (in $):");
        tradeTypeComboBox.setOnAction(e -> {
            if ("Sell".equals(tradeTypeComboBox.getValue())) {
                sellPriceLabel.setVisible(true);
                sellPriceField.setVisible(true);
                quantityLabel.setText("Quantity Sold:");
            } else {
                sellPriceField.setVisible(false);
                sellPriceField.setVisible(false);
                quantityLabel.setText("Quantity Bought:");
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {

            if (datePicker.getValue() == null) {
                CUSTOMALERT.showAlert("Error", "Please select a valid trade date.", Alert.AlertType.ERROR);
                return;
            }
            if (coinNameComboBox.getValue() == null || coinNameComboBox.getValue().isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Please select a coin name.", Alert.AlertType.ERROR);
                return;
            }
            if (tradeTypeComboBox.getValue() == null || tradeTypeComboBox.getValue().isEmpty()) {
                CUSTOMALERT.showAlert("Error", "Please select a trade type (Buy/Sell).", Alert.AlertType.ERROR);
                return;
            }
            if (quantityField.getText().isEmpty() || !quantityField.getText().matches("[0-9]*\\.?[0-9]+")) {
                CUSTOMALERT.showAlert("Error", "Please enter a valid quantity (numeric).", Alert.AlertType.ERROR);
                return;
            }
            if (priceField.getText().isEmpty() || !priceField.getText().matches("[0-9]*\\.?[0-9]+")) {
                CUSTOMALERT.showAlert("Error", "Please enter a valid price (numeric).", Alert.AlertType.ERROR);
                return;
            }
            if (datePicker.getValue().isAfter(LocalDate.now())) {
                CUSTOMALERT.showAlert("Error", "Trade date cannot be greater than today's date.", Alert.AlertType.ERROR);
                return;
            }

            LocalDate tradeDate = datePicker.getValue();
            String coinName = coinNameComboBox.getValue();
            String tradeType = tradeTypeComboBox.getValue();
            double quantity = Double.parseDouble(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            double sellPrice = 0;

            if ("Sell".equals(tradeType)) {
                if (sellPriceField.getText().isEmpty() || !sellPriceField.getText().matches("[0-9]*\\.?[0-9]+")) {
                    CUSTOMALERT.showAlert("Error", "Please enter a valid sell price (numeric).", Alert.AlertType.ERROR);
                    return;
                }
                sellPrice = Double.parseDouble(sellPriceField.getText());
            }
            TradeRecord updatedRecord=null;
            if(sellPrice>0){
                updatedRecord = new TradeRecord(tradeDate,coinName,tradeType,quantity,price,sellPrice);
            }else if(sellPrice==0){
                updatedRecord = new TradeRecord(tradeDate, coinName, tradeType, quantity, price);
            }
            TradeManager.updateRecord(updatedRecord.getTradeDate(),updatedRecord.getCoinName(),updatedRecord.getTradeType(),updatedRecord.getQuantity(),updatedRecord.getBuyPrice(),updatedRecord.getSellPrice(),updatedRecord.getTotalValue(),updatedRecord.getProfitOrLoss(),record.getRecordId());
            CUSTOMALERT.showAlert("Record Updated", "Your new record has been added successfully.", Alert.AlertType.INFORMATION);
            popUpStage.close();
            Portfolio.loadPortfolio();
        });

        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setOnAction(e -> popUpStage.close());

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(15));
        formGrid.add(new Label("Trade Date:"), 0, 0);
        formGrid.add(datePicker, 1, 0);
        formGrid.add(new Label("Coin Name:"), 0, 1);
        formGrid.add(coinNameComboBox, 1, 1);
        formGrid.add(new Label("Trade Type:"), 0, 2);
        formGrid.add(tradeTypeComboBox, 1, 2);
        formGrid.add(quantityLabel, 0, 3);
        formGrid.add(quantityField, 1, 3);
        formGrid.add(priceLabel, 0, 4);
        formGrid.add(priceField, 1, 4);
        formGrid.add(sellPriceLabel, 0, 5);
        formGrid.add(sellPriceField, 1, 5);

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-alignment: center;");


        VBox mainLayout = new VBox(10, formGrid, buttonBox);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Scene scene = new Scene(mainLayout, 400, 400);
        popUpStage.setScene(scene);
        popUpStage.showAndWait();
    }






}

package com.example.crypto_price_tracker.layouts;
import com.example.crypto_price_tracker.models.ChartHistoricalData;
import com.example.crypto_price_tracker.models.CoinDetailsFetcher;
import com.example.crypto_price_tracker.util.CUSTOMALERT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CoinScreenLayout {


    public void loadLayout(String coinID, String coinName, String currency, Double perDayLow, Double perDayHigh) {
        Stage showScreen=CUSTOMALERT.showScreen("Fetching Coin Details. Please Wait...");
        showScreen.show();
        ArrayList<String> coinsDetails = new ArrayList<>();
        try {
            coinsDetails =  CoinDetailsFetcher.fetchCoinDetails(coinID, currency);  
            ChartHistoricalData.fetchAndGenerateChart(coinID,currency);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (coinsDetails.isEmpty()) {
            CUSTOMALERT.showAlert("No Details", "Details about this coin are not available for now. Sorry for the inconvenience.", Alert.AlertType.ERROR);
            return;
        }

        Stage stage = new Stage();


        Image coinImage = new Image(coinsDetails.get(0));
        ImageView coinImageView = new ImageView(coinImage);
        coinImageView.setFitHeight(100);
        coinImageView.setFitWidth(100);
        coinImageView.setPreserveRatio(true);

        Label descriptionLabel = new Label(coinsDetails.get(2));
        descriptionLabel.setId("description");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setPrefWidth(800);
        descriptionLabel.setPrefHeight(50);

        Image chartImage;
        try {
            chartImage = new Image(Objects.requireNonNull(getClass().getResource("/charts/historical_coin_price_chart.png")).toString());
        } catch (Exception e) {
            CUSTOMALERT.showAlert("Chart Error", "Unable to load the chart image. Ensure the file is in the correct location.", Alert.AlertType.ERROR);
            return;
        }

        ImageView chartImageView = new ImageView(chartImage);
        chartImageView.setFitWidth(500);
        chartImageView.setFitHeight(300);
        chartImageView.setPreserveRatio(true);

        Button exitButton = new Button("Exit Coin");
        exitButton.setPrefWidth(150);
        exitButton.setAlignment(Pos.CENTER);
        exitButton.setId("exitButton");
        exitButton.setOnAction(e -> stage.close());

        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        topSection.getChildren().addAll( coinImageView, descriptionLabel,exitButton);

        VBox chartSection = new VBox(20);
        chartSection.setAlignment(Pos.CENTER);
        chartSection.getChildren().add(chartImageView);

        HBox marketRankBox = new HBox(400);
        Label marketRankLabel = new Label("Market Rank");
        Label marketRank = new Label(coinsDetails.get(1));
        marketRankBox.getChildren().addAll(marketRankLabel, marketRank);
        HBox perDayLowBox = new HBox(400);
        Label perDayLowLabel = new Label("24hr Low");
        Label DayLow = new Label(String.valueOf(perDayLow));
        perDayLowBox.getChildren().addAll(perDayLowLabel, DayLow);

        HBox perDayHighBox = new HBox(400);
        Label perDayHighLabel = new Label("24hr High");
        Label dayHigh = new Label(String.valueOf(perDayHigh));
        perDayHighBox.getChildren().addAll(perDayHighLabel, dayHigh);

        HBox websiteLinkBox = new HBox(400);
        Label webLabel = new Label("Visit At");
        Label link = new Label(coinsDetails.get(3));
        websiteLinkBox.getChildren().addAll(webLabel, link);



        VBox bottomSection = new VBox(10);
        bottomSection.setId("bottomSection");
        bottomSection.setPadding(new Insets(0,0,0,315));
        bottomSection.setPrefWidth(500);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.getChildren().addAll(marketRankBox, perDayLowBox, perDayHighBox, websiteLinkBox);

        VBox mainLayout = new VBox(30);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(topSection, chartSection, bottomSection);

        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/coin.css")).toExternalForm());
        stage.setTitle("Coin Details - " + coinName);
        stage.setWidth(1200);
        stage.setHeight(730);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setFullScreen(true);
        stage.show();
        showScreen.close();
    }


}
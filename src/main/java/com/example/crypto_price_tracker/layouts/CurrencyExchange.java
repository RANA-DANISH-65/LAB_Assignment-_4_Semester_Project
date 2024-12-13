package com.example.crypto_price_tracker.layouts;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.concurrent.Task;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

public class CurrencyExchange{

    public static Map<String, Double> getCurrencyExchangeRates(String baseCurrency) {
        Map<String, Double> exchangeRates = new HashMap<>();
        try {
            String apiKey = "e3accbe4235dabde4e5b39f2";
            String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                JSONObject jsonResponse = new JSONObject(content.toString());
                if (jsonResponse.has("conversion_rates")) {
                    JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");
                    Iterator<String> keys = conversionRates.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        double rate = conversionRates.getDouble(key);
                        exchangeRates.put(key, rate);
                    }
                } else {
                    System.out.println("Error: 'conversion_rates' not found in the API response.");
                }
            } else {
                System.out.println("Error: Unable to get data. Response code: " + statusCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }

    public void loadLayout() {
        Stage stage = new Stage();
        Button bt1 = new Button("Home");
        Button bt2 = new Button("Background");
        Button bt3 = new Button("Portfolio");
        Label lbl1 = new Label("CURRENCY EXCHANGE");
        lbl1.setStyle("-fx-font-size :20px");
        Button bt5 = new Button("Signup");
        HBox h1 = new HBox(lbl1, new Region(), new Region(), new Region(), bt1, bt2, bt3, new Region(), new Region(),
                new Region(), new Region(), bt5);
        h1.setSpacing(50);
        h1.getStyleClass().add("navbar");
        h1.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(h1, Priority.ALWAYS);
        h1.setPadding(new Insets(30, 0, 0, 0));
        Label mainheadingLabel = new Label("Global");
        mainheadingLabel.setPadding(new Insets(0, 0, 0, 120));
        Label mainheadingLabel2 = new Label("Currency Exchange Hub");
        VBox mainheadingv = new VBox(mainheadingLabel, mainheadingLabel2);
        mainheadingv.setSpacing(5);
        mainheadingv.setPadding(new Insets(50));
        mainheadingv.getStyleClass().add("mainheading-container");

        Label smallfontLabel = new Label("\"Empowering your trades with precise updates,be the master of currency exchange.");
        Label smallfontLabel2 = new Label("Where currencies meet , Exchange made simple");
        smallfontLabel2.setPadding(new Insets(0, 0, 0, 80));
        smallfontLabel.setPadding(new Insets(0, 0, 0, 15));
        VBox vbboxsmallfont = new VBox(smallfontLabel, smallfontLabel2);
        vbboxsmallfont.setPadding(new Insets(-20, 0, 0, 20));
        vbboxsmallfont.setSpacing(10);

        vbboxsmallfont.getStyleClass().add("Label-smallfont");

        Map<String, Double> exchangeRates = getCurrencyExchangeRates("USD");
        ArrayList<String> currencyList = new ArrayList<>(exchangeRates.keySet());

        ComboBox<String> fromCurrency = new ComboBox<>();
        fromCurrency.getItems().addAll(currencyList);
        fromCurrency.setPromptText("From Currency");

        ComboBox<String> toCurrency = new ComboBox<>();
        toCurrency.getItems().addAll(currencyList);
        toCurrency.setPromptText("To Currency");

        toCurrency.setPrefSize(220, 50);
        fromCurrency.setPrefSize(220, 50);


        HBox tofromBox = new HBox(fromCurrency, toCurrency);
        fromCurrency.getStyleClass().add("comboboxes");
        toCurrency.getStyleClass().add("comboboxes2");

        tofromBox.setPadding(new Insets(50, 0, 0, 30));
        tofromBox.setSpacing(35);

        Button compareButton = new Button("Compare");
        compareButton.getStyleClass().add("comparebutton");
        compareButton.setPrefHeight(30);
        compareButton.setPrefWidth(90);
        HBox comparebuttonbox = new HBox(compareButton);
        comparebuttonbox.setPadding(new Insets(25, 0, 0, 215));

        Label resultLabel = new Label("RESULT :");

        TextField resulttTextField = new TextField();
        resulttTextField.setPrefHeight(50);
        resulttTextField.setPrefWidth(250);
        resultLabel.setStyle("-fx-text-fill:white");
        HBox converterlabelBox = new HBox(resultLabel, resulttTextField);
        converterlabelBox.setPadding(new Insets(50, 0, 0, 35));
        converterlabelBox.setSpacing(10);
        converterlabelBox.getStyleClass().add("result-container");

        String imageurl = "https://cdn-icons-png.flaticon.com/512/5373/5373076.png";
        Image img = new Image(imageurl);
        ImageView imgview = new ImageView(img);

        imgview.setPreserveRatio(true);


        imgview.setFitWidth(450);
        imgview.setFitHeight(450);

        compareButton.setOnAction(e -> {
            String from = fromCurrency.getValue();
            String to = toCurrency.getValue();

            if (from == null || to == null) {
                resulttTextField.setText("Please select both currencies.");
                return;
            }

            if (from.equals(to)) {
                resulttTextField.setText("Both currencies are the same.");
                return;
            }

            double fromRate = exchangeRates.getOrDefault(from, 1.0);
            double toRate = exchangeRates.getOrDefault(to, 1.0);
            double conversionRate = toRate / fromRate;

            resulttTextField.setText(String.format("1 %s = %.4f %s", from, conversionRate, to));

            int userId = 1;
            double fromAmount = 1.0;
            double toAmount = fromAmount * conversionRate;


        });

        GridPane gp = new GridPane();
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(60);

        gp.add(h1, 0, 0);
        gp.add(mainheadingv, 0, 1);
        gp.add(vbboxsmallfont, 0, 2);
        gp.add(comparebuttonbox, 0, 6);
        gp.add(converterlabelBox, 0, 7);
        gp.add(tofromBox, 0, 5);

        gp.add(imgview, 0, 8);


        GridPane.setHalignment(imgview, HPos.CENTER);
        GridPane.setValignment(imgview, VPos.CENTER);

        GridPane.setMargin(imgview, new Insets(-480,0,0,600));

        RowConstraints imageRowConstraints = new RowConstraints();
        imageRowConstraints.setMinHeight(80);
        gp.getRowConstraints().add(imageRowConstraints);



        gp.setMaxWidth(Double.MAX_VALUE);
        gp.setAlignment(Pos.TOP_CENTER);
        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().add(column);

        Scene mainpageScene = new Scene(gp, 1200, 600);
        mainpageScene.getStylesheets().add(getClass().getResource("/styles/Exchange.css").toExternalForm());
        stage.setScene(mainpageScene);

        stage.show();

    }

}
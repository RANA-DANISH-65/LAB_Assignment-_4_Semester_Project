package com.example.crypto_price_tracker.util;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.Random;

public class CUSTOMALERT {

    private static final String[] QUOTES = {
            "“I started a company selling land mines… profits are through the roof.”",
            "“My boss wanted me to start a cryptocurrency mining company. I said, ‘I don’t have the bandwidth.’”",
            "“Bitcoin is like a bank account… if your bank account had a wild personality disorder.”",
            "“I wanted to open a bakery with a crypto theme, but it turned out to be a dough-coin.”",
            "“The only thing more volatile than crypto prices is my mood before coffee.”",
            "“I bought some crypto and now I’m living the dream… of sleeping on my friend’s couch.”",
            "“I tried to explain blockchain to my grandma. She thought it was a new type of knitting technique.”",
            "“My portfolio isn’t losing money, it’s just ‘rebalancing’ itself… all the way down.”",
            "“I’m not saying my business is bad, but my accountant suggested we try hiding from the IRS in the Bahamas.”",
            "“You know you’re a true crypto enthusiast when you check the price of Bitcoin before checking your email.”",
            "“I told my friend I invested in a ‘bit’ of cryptocurrency. He thought I bought a new kitchen appliance.”",
            "“Start a business they said, it’ll be fun they said… Now I have a boss who looks just like me.”",
            "“I asked my bank for a loan. They asked if I had any collateral. I said ‘Just my collection of crypto coins.’”",
            "“If at first you don’t succeed in crypto, just HODL on!”",
            "“I’ve decided to invest all my money into stocks. Specifically, stocks of bread. It’s a kneaded investment.”",
            "“Crypto mining is just like being a detective, but instead of finding criminals, you’re searching for digital gold.”",
            "“I opened a small business selling ‘crypto-tokens.’ Unfortunately, it was just a bag of coins.”",
            "“Running a startup is like being on a rollercoaster: full of highs, lows, and the occasional scream.”",
            "“Why did the crypto investor bring a ladder to the market? To reach the high returns.”",
            "“In the business world, success is like cryptocurrency… it's hard to predict but highly volatile!”"
    };



    public static void showAlert(String title, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static Stage showScreen(String message) {
        Stage stage = new Stage();
        VBox layout = new VBox(30);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setStyle("-fx-background-color: black; -fx-border-width: 2px;");

        Label label = new Label(message);
        label.setStyle("-fx-text-fill: gray; -fx-font-size: 30px;");
        label.setAlignment(javafx.geometry.Pos.CENTER);
        String randomQuote = QUOTES[new Random().nextInt(QUOTES.length)];
        Label quoteLabel = new Label(randomQuote);
        quoteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-style: italic;");
        quoteLabel.setAlignment(Pos.CENTER);
       layout.getChildren().addAll(label, quoteLabel);
        stage.setScene(new Scene(layout));
        stage.setFullScreen(true);
        return stage;
    }
}

package com.example.crypto_price_tracker.layouts;

import com.example.crypto_price_tracker.models.CryptoNewsFetcher;
import com.example.crypto_price_tracker.models.News;
import com.example.crypto_price_tracker.util.CUSTOMALERT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CryptoNewsLayout {
    public void loadNewsLayout() {
        Stage showScreen = CUSTOMALERT.showScreen("Fetching News. Please Wait...");
        showScreen.show();

        CryptoNewsFetcher cryptoNewsFetcher = new CryptoNewsFetcher();
        ArrayList<News> newsItems = cryptoNewsFetcher.getNewsList();

        Label header1 = new Label("In Crypto We Trust");
        header1.setId("header1");
        header1.setAlignment(Pos.CENTER);

        Label header2 = new Label("Until Market Adjusts");
        header2.setId("header2");
        header2.setAlignment(Pos.CENTER);

        Button exitButton = new Button("Exit News");
      exitButton.setId("exitButton");

        FlowPane newsContainer = new FlowPane();
        newsContainer.setId("newsContainer");
        newsContainer.setVgap(20);
        newsContainer.setHgap(20);
        newsContainer.setPadding(new Insets(20, 20, 20, 100));

        newsItems.forEach(newsItem -> {
            String title = newsItem.getTitle() != null ? newsItem.getTitle() : "No Title Available";
            String descriptionText = newsItem.getDescription() != null ? newsItem.getDescription() : "No Description Available";
            String imageUrl = getClass().getResource("/defaultNewsImage.jpg").toExternalForm();
            String source = newsItem.getSourceName() != null ? "Source: " + newsItem.getSourceName() : "Source: Unknown";
            String link = newsItem.getLink() != null ? "Link: " + newsItem.getLink() : "Link: Not Available";

            VBox newsBox = new VBox(10);
            newsBox.setId("newsBox");
            newsBox.setPrefWidth(300);

            Image newsImage = new Image(imageUrl);
            ImageView newsView = new ImageView(newsImage);
            newsView.setFitWidth(300);
            newsView.setFitHeight(200);

            Label titleLabel = new Label(title);
            titleLabel.setId("newsTitle");

            titleLabel.setWrapText(true);
            titleLabel.setPrefWidth(300);
            titleLabel.setAlignment(Pos.CENTER_LEFT);
            titleLabel.setPadding(new Insets(10));

            Label contentLabel = new Label(descriptionText);
            contentLabel.setId("newsDescription");
            contentLabel.setStyle("-fx-text-fill: #ccc;");
            contentLabel.setWrapText(true);
            contentLabel.setPrefHeight(150);
            contentLabel.setPadding(new Insets(10));

            Label sourceLabel = new Label(source);
            sourceLabel.setId("newsSource");
            sourceLabel.setStyle("-fx-text-fill: white;");
            sourceLabel.setPadding(new Insets(10));

            Label linkLabel = new Label(link);
            linkLabel.setId("newsLink");
            linkLabel.setStyle("-fx-text-fill: white;");
            linkLabel.setPadding(new Insets(10));

            newsBox.getChildren().addAll(newsView, titleLabel, contentLabel, sourceLabel, linkLabel);
            newsContainer.getChildren().add(newsBox);
        });

        ScrollPane scrollPane = new ScrollPane(newsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setId("newsScrollPane");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(header1, header2, exitButton, scrollPane);

        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add(getClass().getResource("/styles/news.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Crypto News");
        stage.setScene(scene);
        stage.setFullScreen(true);

        exitButton.setOnMouseClicked(event -> {
            stage.close();
        });

        showScreen.close();
        stage.show();
    }
}

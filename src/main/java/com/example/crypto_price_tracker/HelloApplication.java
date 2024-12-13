package com.example.crypto_price_tracker;
import com.example.crypto_price_tracker.layouts.*;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new homeLayout().loadLayout();
    }
    public static void main(String[] args) {
        launch();
    }
}
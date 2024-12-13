package com.example.crypto_price_tracker.layouts;

import com.example.crypto_price_tracker.models.Coin;
import com.example.crypto_price_tracker.models.CoinsFetcher;
import com.example.crypto_price_tracker.util.CUSTOMALERT;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class homeLayout {
    private static final Map<String, String> CURRENCY_SYMBOLS = Map.of(
            "USD", "$ ",
            "EUR", "€ ",
            "INR", "₹ "
    );

    private static HBox navbar=new HBox();
    public static AtomicReference<String>  selectedCurrency = new AtomicReference<>();
    public static  VBox mainLayout = new VBox();
    public static VBox welcomeSection=new VBox();
    public static HBox searchBox=new HBox();
    private static Stage stage=new Stage();
    public static Button signUpButton;
    private static   TableView<Coin> coinTable=new TableView<Coin>();
    private static ObservableList<Coin> fetchDatafromAPI(String currency) {
        ObservableList<Coin> fetchedCoinsList = FXCollections.observableArrayList();
        fetchedCoinsList = CoinsFetcher.fetchCoinsData(currency);
        return fetchedCoinsList;
    }
    public void loadLayout() {
        loadNavbar();
        HBox.setHgrow(new Region(), Priority.ALWAYS);
        Label title = new Label("Largest");
        title.setId("title");
        Label subtitle = new Label("Crypto Marketplace");
        subtitle.setId("subtitle");
        Label welcomeText = new Label("Welcome to the world's largest cryptocurrency marketplace.");
        welcomeText.setId("welcomeText");
        Label signupPrompt = new Label("Signup to explore more about crypto.");
        signupPrompt.setId("signupPrompt");
        welcomeSection.getChildren().addAll(title, subtitle, welcomeText, signupPrompt);
        welcomeSection.setAlignment(Pos.CENTER);
        welcomeSection.setSpacing(5);

        TextField searchField = new TextField();
        searchField.setId("searchField");
        searchField.setPromptText("Search Crypto...");
        Button searchButton = new Button("Search");
        searchButton.setId("searchButton");
         searchBox.getChildren().addAll(searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(10);

        ObservableList<Coin> fetchedCoinsList = FXCollections.observableArrayList();
        fetchedCoinsList = fetchDatafromAPI(selectedCurrency.get());
        ObservableList<Coin> finalFetchedCoinsList = fetchedCoinsList;

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                updateTable(selectedCurrency.get(), finalFetchedCoinsList, mainLayout);
            } else {
                ObservableList<Coin> filteredCoins = FXCollections.observableArrayList();
                for (Coin coin : finalFetchedCoinsList) {
                    if (coin.getCoinName().toLowerCase().contains(newValue.toLowerCase().trim())) {
                        filteredCoins.add(coin);
                    }
                }
                if (!filteredCoins.isEmpty()) {
                    updateTable(selectedCurrency.get(), filteredCoins, mainLayout);
                }
            }
        });

        searchButton.setOnAction(e -> {
            String searchQuery = searchField.getText().toLowerCase().trim();
            ObservableList<Coin> filteredCoins = FXCollections.observableArrayList();
            for (Coin coin : finalFetchedCoinsList) {
                if (coin.getCoinName().toLowerCase().contains(searchQuery)) {
                    filteredCoins.add(coin);
                }
            }
            if (!filteredCoins.isEmpty()) {
                updateTable(selectedCurrency.get(), filteredCoins, mainLayout);
            } else {
                CUSTOMALERT.showAlert("Not Found", "The entered Coin is not Found", Alert.AlertType.ERROR);
            }
        });


        mainLayout.getChildren().addAll(navbar, welcomeSection, searchBox);
        updateTable(selectedCurrency.get(), fetchedCoinsList, mainLayout);
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10,20,20,20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(mainLayout, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/home.css").toExternalForm());
        stage.setTitle("TokenTribe");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }



    private  TableView<Coin> createCoinTable(ObservableList<Coin> fetchedCoinsList, String selectedCurrency) {
        TableView<Coin> coinTable = new TableView<>();
        coinTable.setPrefWidth(800);
        coinTable.setPrefHeight(300);
        coinTable.setEditable(false);
        coinTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Coin, String> nameColumn = new TableColumn<>("Coin Name");
        nameColumn.setPrefWidth(200);
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().coinNameProperty());
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 0 0 80;");
        TableColumn<Coin, ImageView> imageColumn = new TableColumn<>("Coin");
        imageColumn.setPrefWidth(100);
        imageColumn.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 0 0 80;");
        imageColumn.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView();
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    return new Image(cellData.getValue().getCoinImageUrl(), false);
                }
            };
            loadImageTask.setOnSucceeded(e -> imageView.setImage(loadImageTask.getValue()));
            loadImageTask.setOnFailed(e -> imageView.setImage(new Image(getClass().getResource("/default-coin.jpeg").toExternalForm())));
            new Thread(loadImageTask).start();
            return new SimpleObjectProperty<>(imageView);
        });

        TableColumn<Coin, Double> priceColumn = new TableColumn<>("Current Price");
        priceColumn.setPrefWidth(150);
        priceColumn.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 0 0 80;");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().currentPriceProperty().asObject());

        priceColumn.setCellFactory(col -> {
            return new TableCell<Coin, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        String currencySymbol = CURRENCY_SYMBOLS.getOrDefault(selectedCurrency, "$ ");
                        setText(String.format("%s%.1f", currencySymbol, item));
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            };
        });
        coinTable.setOnMouseClicked(event -> {
            if(Authentication.isAuthenticated){
                Coin selectedCoin = coinTable.getSelectionModel().getSelectedItem();
                if (selectedCoin != null) {
                    System.out.println("Coin ID: " + selectedCoin.getCoinID());
                    System.out.println("Selected Currency: " + selectedCurrency);
                    CoinScreenLayout coinLayout = new CoinScreenLayout();
                    coinLayout.loadLayout(selectedCoin.getCoinID(), selectedCoin.getCoinName(), selectedCurrency, selectedCoin.getPerDayLow(), selectedCoin.perDayLowProperty().get());
                }
            }else{
                CUSTOMALERT.showAlert("Authentication Required", "You need to be authenticated to details of Coin", Alert.AlertType.ERROR);
            }

        });

        TableColumn<Coin, Double> changeColumn = new TableColumn<>("24h Change");
        changeColumn.setPrefWidth(150);
        changeColumn.setCellValueFactory(cellData -> cellData.getValue().percentageChange24hProperty().asObject());
        changeColumn.setCellFactory(col -> {
            return new TableCell<Coin, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.format("%.2f%%", item));
                        if (item < 0) {
                            setStyle("-fx-text-fill: red; -fx-alignment: CENTER; -fx-padding: 0 60 0 0;");
                        } else if (item > 0) {
                            setStyle("-fx-text-fill: green; -fx-alignment: CENTER; -fx-padding: 0 60 0 0;");
                        } else {
                            setStyle("-fx-text-fill: gray; -fx-alignment: CENTER; -fx-padding: 0 60 0 0;");
                        }
                    }
                }
            };
        });

        TableColumn<Coin, Double> marketCapColumn = new TableColumn<>("Market Cap");
        marketCapColumn.setPrefWidth(200);
        marketCapColumn.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 0 0 30;");
        marketCapColumn.setCellValueFactory(cellData -> cellData.getValue().marketCapProperty().asObject());
        marketCapColumn.setCellFactory(col -> {
            return new TableCell<Coin, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.format("%.2f%%", item));
                    }
                }
            };
        });
        coinTable.getColumns().addAll(nameColumn, imageColumn, priceColumn, changeColumn, marketCapColumn);
        coinTable.setItems(fetchedCoinsList);
        return coinTable;
    }

    private static void updateTable(String selectedCurrency, VBox mainLayout) {
        ObservableList<Coin> fetchedCoinsList = fetchDatafromAPI(selectedCurrency);
        mainLayout.getChildren().removeIf(node -> node instanceof TableView);
        homeLayout homeLayout=new homeLayout();
        TableView<Coin> coinTable =homeLayout.createCoinTable(fetchedCoinsList, selectedCurrency);
        mainLayout.getChildren().add(coinTable);
    }

    private void updateTable(String selectedCurrency, ObservableList fetchedCoinsList, VBox mainLayout) {
        mainLayout.getChildren().removeIf(node -> node instanceof TableView);
        coinTable = createCoinTable(fetchedCoinsList, selectedCurrency);
        mainLayout.getChildren().add(coinTable);
    }


    public static void loadNavbar(){
        Label logo = new Label("Coin Flipper");
        logo.setId("logo");
        Button newsButton = new Button("Get News");
        newsButton.setOnAction(e -> {
            if(Authentication.isAuthenticated){
                   new CryptoNewsLayout().loadNewsLayout();
            }else{
                CUSTOMALERT.showAlert("Sign In","Please sign in to read News",Alert.AlertType.ERROR);
            }

        });
        Button portfolioButton = new Button("Portfolio");
        portfolioButton.setOnAction(e -> {
            if(Authentication.isAuthenticated){
                Portfolio.loadPortfolio();
            }else{
                CUSTOMALERT.showAlert("Authentication Required","You need to be authenticated to view Portfolio",Alert.AlertType.ERROR);
            }
        });
        ComboBox<String> currencyList = new ComboBox<>();
        currencyList.setId("currencyList");
        currencyList.getItems().addAll("USD", "EUR", "INR");
        currencyList.setValue("USD");
        selectedCurrency = new AtomicReference<>(currencyList.getValue());

        currencyList.valueProperty().addListener((obs, oldVal, newVal) -> {
            selectedCurrency.set(newVal);
            updateTable(selectedCurrency.get(), mainLayout);
        });

        signUpButton = new Button();
        if(Authentication.isAuthenticated){
            signUpButton.setText("Sign Out");
        }else{
            signUpButton.setText("Sign Up");
        }
        signUpButton.setOnAction(e->{
            if(!Authentication.isAuthenticated) {
                Authentication auth = new Authentication();
                auth.loadSignUpLayout();
            }else{
                Authentication.isAuthenticated = false;
                loadNavbar();
                updateStage();
            };
        });
        Button changePasswordButton =new Button("Change Password");
        changePasswordButton.setId("changePasswordButton");
        changePasswordButton.setOnAction(e->{
            new Authentication().loadChangePasswordLayout();
        });
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e->{
            stage.close();
        });
        navbar.getChildren().clear();


        if(Authentication.isAuthenticated){
            navbar.getChildren().addAll( newsButton, portfolioButton,  currencyList, signUpButton,changePasswordButton, exitButton);
        }else{
            navbar.getChildren().addAll(logo,new Region(), newsButton, portfolioButton, currencyList, signUpButton, exitButton);
        }
        navbar.setId("navbar");
        navbar.setSpacing(50);
        navbar.setAlignment(Pos.CENTER);
    }

    public static void updateStage(){
        mainLayout.getChildren().clear();
        mainLayout.getChildren().addAll(navbar, welcomeSection, searchBox,coinTable);
    }
}

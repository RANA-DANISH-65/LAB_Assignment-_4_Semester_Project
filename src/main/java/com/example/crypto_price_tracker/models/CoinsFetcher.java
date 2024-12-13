package com.example.crypto_price_tracker.models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
public class CoinsFetcher {
    public static ObservableList<Coin> fetchCoinsData(String currency) {
        OkHttpClient client = new OkHttpClient();
        System.out.println("Request Made Coin List API");
        Request request = new Request.Builder()
                .url("https://api.coingecko.com/api/v3/coins/markets?vs_currency=" + currency)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-api-key", "CG-qG85zkoPRrAkMh2tPJ6ryNvg")
                .build();
        Response response = null;
        try{
            response = client.newCall(request).execute();
            System.out.println("Response: " + response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (response.isSuccessful()) {
            assert response.body() != null;
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response.body().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ObservableList<Coin> coins = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCoin = jsonArray.getJSONObject(i);
                String coinID = jsonCoin.optString("id", "Unknown");
                String coinName = jsonCoin.optString("name", "Unknown");
                double currentPrice = jsonCoin.optDouble("current_price", 0.0);
                double percentageChange24h = jsonCoin.optDouble("price_change_percentage_24h", 0.0);
                double marketCap = jsonCoin.optDouble("market_cap", 0.0);
                String coinImageUrl = jsonCoin.optString("image", "");
                int marketRank = jsonCoin.optInt("market_cap_rank", 0);
                double perDayHigh = jsonCoin.optDouble("high_24h", 0.0);
                double perDayLow = jsonCoin.optDouble("low_24h", 0.0);
                Coin coin = new Coin(coinID, coinName, currentPrice, percentageChange24h, marketCap,
                        coinImageUrl, marketRank, perDayHigh, perDayLow);
                coins.add(coin);
            }
            return coins;
        } else {
            try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

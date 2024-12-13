package com.example.crypto_price_tracker.models;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.util.ArrayList;

public class CoinDetailsFetcher {
    public static ArrayList<String> fetchCoinDetails(String coinID ,String currency) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.coingecko.com/api/v3/coins/" + coinID)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-api-key", "CG-qG85zkoPRrAkMh2tPJ6ryNvg")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            assert response.body() != null;
            JSONObject jsonResponse = new JSONObject(response.body().string());
            String largeImageUrl = jsonResponse.getJSONObject("image").getString("large");
            String marketRank = String.valueOf(jsonResponse.getInt("market_cap_rank"));
            String description = jsonResponse.getJSONObject("description").optString("en","Description of Coin is not Available");
            String websiteLink = jsonResponse.getJSONObject("links").getJSONArray("homepage").optString(0,"Not Available");
            ArrayList<String> coinsDetails=new ArrayList<String>();
            coinsDetails.add(largeImageUrl);
            coinsDetails.add(marketRank);
            coinsDetails.add(description);
            coinsDetails.add(websiteLink);
            return coinsDetails;
        } else {
            throw new Exception("Unexpected response: " + response);
        }
    }
}

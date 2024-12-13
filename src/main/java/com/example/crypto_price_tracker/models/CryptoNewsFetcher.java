package com.example.crypto_price_tracker.models;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
public class CryptoNewsFetcher {
    private final ArrayList<News> newsList;
    public CryptoNewsFetcher() {
        this.newsList = new ArrayList<>();
        fetchNewsFromAPI();
    }
    public void addNews(String title, String description, String sourceName, String imageUrl, String link) {
        News news = new News(title, description, sourceName, imageUrl, link);
        newsList.add(news);
    }
    public ArrayList<News> getNewsList() {
        return newsList;
    }
    public void fetchNewsFromAPI() {
        String apiUrl ="https://newsdata.io/api/1/latest?apikey=pub_51089f7f1f7851c261f9e490f8852c7f37612&q=finance&language=en&country=us";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray articles = jsonResponse.getJSONArray("results");
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String title = article.optString("title", "No title available");
                    String description = article.optString("description", "No description available");
                    String sourceName = article.optString("source", "Unknown source");
                    String imageUrl = article.optString("image_url", "");
                    String link = article.optString("link", "No link available");
                    addNews(title, description, sourceName, imageUrl, link);
                }
            } else {
                System.err.println("Error: " + response.message());
            }
        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

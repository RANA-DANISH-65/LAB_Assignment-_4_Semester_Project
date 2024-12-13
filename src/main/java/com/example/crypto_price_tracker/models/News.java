package com.example.crypto_price_tracker.models;
public class News {
    private String title;
    private String description;
    private String sourceName;
    private String imageUrl;
    private String link;
    public News(String title, String description, String sourceName, String imageUrl, String link) {
        this.title = title;
        this.description = description;
        this.sourceName = sourceName;
        this.imageUrl = imageUrl;
        this.link = link;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}


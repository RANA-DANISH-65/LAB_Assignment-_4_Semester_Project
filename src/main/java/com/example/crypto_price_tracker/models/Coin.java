package com.example.crypto_price_tracker.models;
import javafx.beans.property.*;
public class Coin {
    private StringProperty coinName = new SimpleStringProperty();
    private StringProperty coinID = new SimpleStringProperty();
    private DoubleProperty currentPrice = new SimpleDoubleProperty();
    private DoubleProperty percentageChange24h = new SimpleDoubleProperty();
    private DoubleProperty marketCap = new SimpleDoubleProperty();
    private IntegerProperty marketRank = new SimpleIntegerProperty();
    private DoubleProperty perDayHigh = new SimpleDoubleProperty();
    private DoubleProperty perDayLow = new SimpleDoubleProperty();
    private StringProperty coinImageUrl = new SimpleStringProperty();
    public Coin(String coinID, String coinName, double currentPrice, double percentageChange24h, double marketCap,
                String coinImageUrl, int marketRank, double perDayHigh, double perDayLow) {
        this.coinID.set(coinID);
        this.coinName.set(coinName);
        this.currentPrice.set(currentPrice);
        this.percentageChange24h.set(percentageChange24h);
        this.marketCap.set(marketCap);
        this.coinImageUrl.set(coinImageUrl);
        this.marketRank.set(marketRank);
        this.perDayHigh.set(perDayHigh);
        this.perDayLow.set(perDayLow);
    }
    public StringProperty coinNameProperty() {
        return coinName;
    }
    public String getCoinName() {
        return coinName.get();
    }
    public void setCoinName(String coinName) {
        this.coinName.set(coinName);
    }
    public StringProperty coinIDProperty() {
        return coinID;
    }
    public String getCoinID() {
        return coinID.get();
    }
    public void setCoinID(String coinID) {
        this.coinID.set(coinID);
    }
    public DoubleProperty currentPriceProperty() {
        return currentPrice;
    }
    public double getCurrentPrice() {
        return currentPrice.get();
    }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice.set(currentPrice);
    }
    public DoubleProperty percentageChange24hProperty() {
        return percentageChange24h;
    }
    public double getPercentageChange24h() {
        return percentageChange24h.get();
    }
    public void setPercentageChange24h(double percentageChange24h) {
        this.percentageChange24h.set(percentageChange24h);
    }
    public DoubleProperty marketCapProperty() {
        return marketCap;
    }
    public double getMarketCap() {
        return marketCap.get();
    }
    public void setMarketCap(double marketCap) {
        this.marketCap.set(marketCap);
    }
    public IntegerProperty marketRankProperty() {
        return marketRank;
    }
    public int getMarketRank() {
        return marketRank.get();
    }
    public void setMarketRank(int marketRank) {
        this.marketRank.set(marketRank);
    }
    public DoubleProperty perDayHighProperty() {
        return perDayHigh;
    }
    public double getPerDayHigh() {
        return perDayHigh.get();
    }
    public void setPerDayHigh(double perDayHigh) {
        this.perDayHigh.set(perDayHigh);
    }
    public DoubleProperty perDayLowProperty() {
        return perDayLow;
    }
    public double getPerDayLow() {
        return perDayLow.get();
    }
    public void setPerDayLow(double perDayLow) {
        this.perDayLow.set(perDayLow);
    }
    public StringProperty coinImageUrlProperty() {
        return coinImageUrl;
    }
    public String getCoinImageUrl() {
        return coinImageUrl.get();
    }
    public void setCoinImageUrl(String coinImageUrl) {
        this.coinImageUrl.set(coinImageUrl);
    }
    @Override
    public String toString() {
        return "Coin{" +
                "coinID='" + coinID.get() + '\'' +
                ", coinName='" + coinName.get() + '\'' +
                ", currentPrice=" + currentPrice.get() +
                ", percentageChange24h=" + percentageChange24h.get() +
                ", marketCap=" + marketCap.get() +
                ", marketRank=" + marketRank.get() +
                ", perDayHigh=" + perDayHigh.get() +
                ", perDayLow=" + perDayLow.get() +
                ", coinImageUrl='" + coinImageUrl.get() + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coin)) return false;
        Coin coin = (Coin) obj;
        return coinName.get().equals(coin.coinName.get());
    }
    @Override
    public int hashCode() {
        return coinName.get().hashCode();
    }}


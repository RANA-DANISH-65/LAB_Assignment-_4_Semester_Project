package com.example.crypto_price_tracker.models;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;
public class TradeRecord {
    private int recordId;
    private LocalDate tradeDate;
    private String coinName;
    private String tradeType;
    private double quantity;
    private double buyPrice;
    private double sellPrice;
    private double totalValue;
    private double profitOrLoss;

    public TradeRecord(LocalDate tradeDate, String coinName, String tradeType, double quantity, double buyPrice) {
        this.tradeDate = tradeDate;
        this.coinName = coinName;
        this.tradeType = tradeType;
        this.quantity = roundOff(quantity);
        this.buyPrice = roundOff(buyPrice);
        this.totalValue = roundOff(calculateTotalValue());
        this.sellPrice = 0.0;
        this.profitOrLoss = 0.0;
    }
    public TradeRecord(LocalDate tradeDate, String coinName, String tradeType, double quantity, double buyPrice, double sellPrice) {
        this.tradeDate = tradeDate;
        this.coinName = coinName;
        this.tradeType = tradeType;
        this.quantity = roundOff(quantity);
        this.buyPrice = roundOff(buyPrice);
        this.sellPrice = roundOff(sellPrice);
        this.totalValue = roundOff(calculateTotalValue());
        if (this.tradeType.equalsIgnoreCase("Sell")) {
            this.profitOrLoss = roundOff(calculateProfitOrLoss());
        } else {
            this.profitOrLoss = 0.0;
        }
    }
    public TradeRecord(LocalDate tradeDate, String coinName, String tradeType, double quantity, double buyPrice, double sellPrice, double totalValue, double profitOrLoss, int recordId) {
        this.tradeDate = tradeDate;
        this.coinName = coinName;
        this.tradeType = tradeType;
        this.quantity = roundOff(quantity);
        this.buyPrice = roundOff(buyPrice);
        this.sellPrice = roundOff(sellPrice);
        this.totalValue = roundOff(totalValue);
        this.profitOrLoss = roundOff(profitOrLoss);
        this.recordId = recordId;
    }
    private double calculateTotalValue() {
        if (this.tradeType.equalsIgnoreCase("Sell")) {
            return roundOff(this.quantity * this.sellPrice);
        }
        return roundOff(this.quantity * this.buyPrice);
    }

    private double calculateProfitOrLoss() {
        if (this.tradeType.equalsIgnoreCase("Sell")) {
            return roundOff((this.sellPrice - this.buyPrice) * this.quantity);
        }
        return 0.0;
    }

    private double roundOff(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }
    public int getRecordId() {
        return recordId;
    }
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }
    public String getCoinName() {
        return coinName;
    }
    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
    public String getTradeType() {
        return tradeType;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = roundOff(quantity);
    }
    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = roundOff(buyPrice);
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = roundOff(sellPrice);
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = roundOff(totalValue);
    }

    public double getProfitOrLoss() {
        return profitOrLoss;
    }

    public void setProfitOrLoss(double profitOrLoss) {
        this.profitOrLoss = roundOff(profitOrLoss);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeRecord)) return false;
        TradeRecord that = (TradeRecord) o;
        return this.recordId == that.recordId;
    }

    @Override
    public String toString() {
        return recordId + "|" + tradeDate + "|" + coinName + "|" + tradeType + "|" + quantity + "|" + buyPrice + "|" + sellPrice + "|"
                + totalValue + "|" + profitOrLoss;
    }
}

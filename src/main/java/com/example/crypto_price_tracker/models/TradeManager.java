package com.example.crypto_price_tracker.models;
import com.example.crypto_price_tracker.util.PortfolioOperationHandler;
import com.example.crypto_price_tracker.util.UserDAO;
import java.time.LocalDate;

public class TradeManager {
    public static void addRecord(TradeRecord record) {
        PortfolioOperationHandler.addRecord(UserDAO.getCurrentUser().getUserEmail(),record.getCoinName(),String.valueOf(record.getTradeDate()), record.getTradeType(), record.getQuantity(), record.getBuyPrice(), record.getSellPrice(), record.getTotalValue(), record.getProfitOrLoss());
        PortfolioOperationHandler.loadRecordsFromDatabase(UserDAO.getCurrentUser().getUserEmail());
    }


    public static boolean deleteRecord(int recordID){
        boolean found=false;
       for (TradeRecord record: PortfolioOperationHandler.tradeRecords){
           if(record.getRecordId()==recordID){
               PortfolioOperationHandler.deleteRecord(UserDAO.getCurrentUser().getUserEmail(), recordID);
               PortfolioOperationHandler.loadRecordsFromDatabase(UserDAO.getCurrentUser().getUserEmail());
               found=true;
               return found;
           }
       }
       return found;
    }



    public static void updateRecord(LocalDate tradeDate,String coinName,String tradeType,double quantity,double buyPRice,double sellPrice,double totalValue,double profitLoss,int recordId){
        PortfolioOperationHandler.updateRecord(UserDAO.getCurrentUser().getUserEmail(),recordId, coinName,String.valueOf(tradeDate),tradeType,quantity,buyPRice,sellPrice,totalValue,profitLoss);
        PortfolioOperationHandler.loadRecordsFromDatabase(UserDAO.getCurrentUser().getUserEmail());
    }


    public static String[] getSummary() {
        String[] summary = new String[7];
        int totalTrades = PortfolioOperationHandler.tradeRecords.size();
        double totalValue = 0;
        double totalProfit = 0;
        double totalLoss = 0;
        int winningTrades = 0;
        for (TradeRecord record : PortfolioOperationHandler.tradeRecords) {
            totalValue += record.getTotalValue();
            double profitOrLoss = record.getProfitOrLoss();
            if (profitOrLoss > 0) {
                totalProfit += profitOrLoss;
                winningTrades++;
            } else {
                totalLoss += profitOrLoss;
            }
        }
        double  averageProfit = (totalTrades > 0) ? totalProfit / totalTrades : 0;
        double averageLoss = (totalTrades > 0) ? totalLoss / totalTrades : 0;
        double winProbability = (totalTrades > 0) ? (winningTrades * 100) / totalTrades : 0;
        summary[0] = "Total Trades: " + totalTrades;
        summary[1] = "Total Value: $" + totalValue;
        summary[2] = "Total Profit: $" + totalProfit;
        summary[3] = "Total Loss: $" + totalLoss;
        summary[4] = "Average Profit: $" + averageProfit;
        summary[5] = "Average Loss: $" + averageLoss;
        summary[6] = "Win Probability: " + winProbability + "%";
        return summary;
    }

}

package com.example.crypto_price_tracker.models;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.title.LegendTitle;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartHistoricalData {
    private static final String API_KEY = "CG-qG85zkoPRrAkMh2tPJ6ryNvg";
    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";
    public static void fetchAndGenerateChart(String coinID,String currency) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "coins/" + coinID + "/market_chart?vs_currency=" +currency +"&days=7")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-api-key", API_KEY)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            assert response.body() != null;
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray priceData = jsonResponse.getJSONArray("prices");
            List<Double> prices = new ArrayList<>();
            List<Long> timestamps = new ArrayList<>();
            for (int i = 0; i < priceData.length(); i++) {
                JSONArray dataPoint = priceData.getJSONArray(i);
                long timestamp = dataPoint.getLong(0);
                double price = dataPoint.getDouble(1);
                timestamps.add(timestamp);
                prices.add(price);
            }
            saveChartAsImage(timestamps, prices,currency);

        } else {
            throw new Exception("Failed to fetch historical data");
        }
    }
    public static void saveChartAsImage(List<Long> timestamps, List<Double> prices,String currency) throws IOException {
        TimeSeries priceSeries = new TimeSeries("Price ("+currency.toUpperCase()+")");
        for (int i = 0; i < timestamps.size(); i++) {
            long timestamp = timestamps.get(i);
            double price = prices.get(i);
            priceSeries.addOrUpdate(new Second(new java.util.Date(timestamp)), price);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection(priceSeries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Historical Coin Price",
                "Time",
                 currency.toUpperCase(),
                dataset,
                false,
                true,
                false
        );

        chart.setBackgroundPaint(new Color(169, 169, 169));

        Plot plot = chart.getPlot();
        if (plot instanceof XYPlot) {
            XYPlot xyPlot = (XYPlot) plot;

            xyPlot.setBackgroundPaint(new Color(50, 50, 50));

            xyPlot.setDomainGridlinePaint(new Color(200, 200, 200));
            xyPlot.setRangeGridlinePaint(new Color(200, 200, 200));


            chart.getTitle().setPaint(new Color(255, 255, 255));

            LegendTitle legend = chart.getLegend();
            if (legend == null) {
                chart.addLegend(new LegendTitle(chart.getPlot()));
            }
            if (chart.getLegend() != null) {
                chart.getLegend().setItemPaint(new Color(255, 0, 0));
            }
            xyPlot.getDomainAxis().setLabelPaint(new Color(0, 255, 255));
            xyPlot.getDomainAxis().setTickLabelPaint(new Color(0, 255, 255));


            xyPlot.getRangeAxis().setLabelPaint(new Color(0, 255, 255));
            xyPlot.getRangeAxis().setTickLabelPaint(new Color(0, 255, 255));
        }


        File outputFile = new File("src/main/resources/charts/historical_coin_price_chart.png");
        if (outputFile.exists()) {
            boolean deleted = outputFile.delete();
            if (deleted) {
                System.out.println("Previous chart image deleted.");
            } else {
                System.out.println("Failed to delete the previous chart image.");
            }
        }
        ChartUtils.saveChartAsPNG(outputFile, chart, 800, 500);
        System.out.println("Chart saved as image: " + outputFile.getAbsolutePath());
    }

}

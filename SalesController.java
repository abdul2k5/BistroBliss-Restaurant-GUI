package com.example.bistrobliss;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SalesController {

    @FXML
    private LineChart<String, Number> salesChart;

    @FXML
    public void initialise() {
        loadOrderNumbers();
    }

    private void loadOrderNumbers() {
        try {
            //read order numbers from file
            List<String> orders = Files.readAllLines(Paths.get("Orders.txt"));
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Order Numbers");

            //add order numbers to the series
            for (int i = 0; i < orders.size(); i++) {
                String[] orderData = orders.get(i).split(", ");
                int orderNumber = Integer.parseInt(orderData[0]);
                series.getData().add(new XYChart.Data<>(String.valueOf(orderNumber), i + 1));
            }

            //add series to the chart
            salesChart.getData().add(series);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

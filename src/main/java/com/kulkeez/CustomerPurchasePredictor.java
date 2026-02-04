package com.kulkeez.regression.prediction;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CustomerPurchasePredictor {

    public static void main(String[] args) {
        try {
            // 1. Load the dataset
            List<String[]> data = loadCSV("src/main/resources/customer_purchases.csv");
            
            // 2. Prepare the regression model
            SimpleRegression regression = new SimpleRegression();
            
            // Skip header row and add data points
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                double income = Double.parseDouble(row[2]);  // Independent variable (X)
                double purchaseAmount = Double.parseDouble(row[3]);  // Dependent variable (Y)
                regression.addData(income, purchaseAmount);
            }
            
            // 3. Print model statistics
            System.out.println("=== Model Summary ===");
            System.out.printf("R-squared: %.4f\n", regression.getRSquare());
            System.out.printf("Intercept: %.2f\n", regression.getIntercept());
            System.out.printf("Slope: %.4f\n", regression.getSlope());
            System.out.printf("Standard Error: %.4f\n\n", regression.getRegressionSumSquares());
            
            // 4. Make predictions for new customers
            System.out.println("=== Predictions ===");
            predictPurchase(regression, 40000);  // $40,000 income
            predictPurchase(regression, 55000);  // $55,000 income
            predictPurchase(regression, 80000);  // $80,000 income
            
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
    
    private static List<String[]> loadCSV(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        }
    }
    
    private static void predictPurchase(SimpleRegression regression, double income) {
        double predictedAmount = regression.predict(income);
        System.out.printf("Predicted purchase for $%,.2f income: $%,.2f\n", 
                         income, predictedAmount);
    }
}
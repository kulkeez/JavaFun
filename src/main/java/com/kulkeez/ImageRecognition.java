package com.kulkeez;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.List;


/**
 * Product Image Recognition utility that demonstrates basic image processing techniques 
 * to analyze and classify product images.
 * A simplified lab for beginners to understand image processing concepts
 * without complex dependencies.
 */

public class ImageRecognition {

    // Constants for image analysis
    // private static final int SAMPLE_SIZE = 10;
    private static final int RESIZE_WIDTH = 100;
    private static final int RESIZE_HEIGHT = 100;

    /**
     * Main method to execute the image recognition process
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting Product Recognition Lab");

            // Step 1: Create necessary directories
            String imageDir = "src/main/resources/images";
            File imagesFolder = new File(imageDir);

            Path outputDir = Paths.get("output");
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            // Step 2: Process each image in the folder
            File[] imageFiles = imagesFolder.listFiles();
            if (imageFiles != null && imageFiles.length > 0) {
                for (File imageFile : imageFiles) {
                    if (imageFile.isFile() && (imageFile.getName().endsWith(".jpg") ||
                            imageFile.getName().endsWith(".jpeg") ||
                            imageFile.getName().endsWith(".png"))) {
                        processImage(imageFile, outputDir);
                    }
                }
            } else {
                System.out.println("No images found in directory: " + imageDir);
                System.out.println("Please add some product images to the 'images' folder.");
            }

            System.out.println("Product recognition completed successfully!");

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Process a single image file
     */
    private static void processImage(File imageFile, Path outputDir) throws IOException {
        System.out.println("Analyzing image: " + imageFile.getName());

        // Step 1: Load the image
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            System.err.println("Failed to load image: " + imageFile.getName());
            return;
        }

        // Step 2: Extract image features
        Map<String, Double> features = extractImageFeatures(originalImage);

        // Step 3: Classify the image based on features
        List<Prediction> predictions = classifyImage(features, imageFile.getName());

        // Step 4: Print the results
        System.out.println("Top 5 predictions for " + imageFile.getName() + ":");
        for (Prediction p : predictions) {
            System.out.printf("%-30s: %.2f%%\n", p.getLabel(), p.getProbability() * 100);
        }
        System.out.println("-----------------------------------------\n");

        // Step 5: Save the results to a file
        saveResultsToFile(imageFile.getName(), predictions, outputDir);

        // Optional: Save a processed version of the image
        saveProcessedImage(originalImage, imageFile.getName(), outputDir);
    }

    /**
     * Extract basic features from the image
     */
    private static Map<String, Double> extractImageFeatures(BufferedImage image) {
        Map<String, Double> features = new HashMap<>();

        // Resize image for consistent analysis
        BufferedImage resized = resizeImage(image, RESIZE_WIDTH, RESIZE_HEIGHT);

        // Calculate average color components
        double avgRed = 0;
        double avgGreen = 0;
        double avgBlue = 0;

        // Calculate brightness histogram
        int[] brightnessHistogram = new int[256];

        // Sample pixels from the image
        for (int y = 0; y < resized.getHeight(); y++) {
            for (int x = 0; x < resized.getWidth(); x++) {
                Color color = new Color(resized.getRGB(x, y));
                avgRed += color.getRed();
                avgGreen += color.getGreen();
                avgBlue += color.getBlue();

                // Calculate brightness (simple average of RGB)
                int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                brightnessHistogram[brightness]++;
            }
        }

        int totalPixels = resized.getWidth() * resized.getHeight();
        avgRed /= totalPixels;
        avgGreen /= totalPixels;
        avgBlue /= totalPixels;

        features.put("avgRed", avgRed / 255.0);
        features.put("avgGreen", avgGreen / 255.0);
        features.put("avgBlue", avgBlue / 255.0);

        // Calculate color ratios
        double redGreenRatio = avgRed / (avgGreen + 1);
        double blueGreenRatio = avgBlue / (avgGreen + 1);
        double redBlueRatio = avgRed / (avgBlue + 1);

        features.put("redGreenRatio", redGreenRatio);
        features.put("blueGreenRatio", blueGreenRatio);
        features.put("redBlueRatio", redBlueRatio);

        // Calculate brightness stats
        double avgBrightness = (avgRed + avgGreen + avgBlue) / 3 / 255.0;
        features.put("avgBrightness", avgBrightness);

        // Analyze edge density (simplified)
        double edgeDensity = calculateEdgeDensity(resized);
        features.put("edgeDensity", edgeDensity);

        // Calculate texture uniformity (simplified)
        double textureUniformity = calculateTextureUniformity(brightnessHistogram, totalPixels);
        features.put("textureUniformity", textureUniformity);

        return features;
    }

    /**
     * Resize an image to specified dimensions
     */
    private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }

    /**
     * Calculate a simple edge density metric
     */
    private static double calculateEdgeDensity(BufferedImage image) {
        int edgeCount = 0;
        int totalPixels = (image.getWidth() - 1) * (image.getHeight() - 1);

        for (int y = 0; y < image.getHeight() - 1; y++) {
            for (int x = 0; x < image.getWidth() - 1; x++) {
                Color pixel = new Color(image.getRGB(x, y));
                Color pixelRight = new Color(image.getRGB(x + 1, y));
                Color pixelBelow = new Color(image.getRGB(x, y + 1));

                int diffX = Math.abs(pixel.getRed() - pixelRight.getRed()) +
                        Math.abs(pixel.getGreen() - pixelRight.getGreen()) +
                        Math.abs(pixel.getBlue() - pixelRight.getBlue());

                int diffY = Math.abs(pixel.getRed() - pixelBelow.getRed()) +
                        Math.abs(pixel.getGreen() - pixelBelow.getGreen()) +
                        Math.abs(pixel.getBlue() - pixelBelow.getBlue());

                // If the difference is significant, count as an edge
                if (diffX > 100 || diffY > 100) {
                    edgeCount++;
                }
            }
        }

        return (double) edgeCount / totalPixels;
    }

    /**
     * Calculate texture uniformity based on brightness histogram
     */
    private static double calculateTextureUniformity(int[] histogram, int totalPixels) {
        double uniformity = 0;
        for (int i = 0; i < histogram.length; i++) {
            double p = (double) histogram[i] / totalPixels;
            uniformity += p * p;
        }
        return uniformity;
    }

    /**
     * Classify the image based on extracted features
     */
    private static List<Prediction> classifyImage(Map<String, Double> features, String filename) {
        List<Prediction> predictions = new ArrayList<>();

        // Using filename for simulation, since this is just a demonstration
        filename = filename.toLowerCase();

        // Initialize with low probabilities
        predictions.add(new Prediction("laptop", 0.01));
        predictions.add(new Prediction("water bottle", 0.01));
        predictions.add(new Prediction("coffee mug", 0.01));
        predictions.add(new Prediction("book", 0.01));
        predictions.add(new Prediction("smartphone", 0.01));

        // Feature-based classification (simplified rules)
        double avgRed = features.get("avgRed");
        double avgGreen = features.get("avgGreen");
        double avgBlue = features.get("avgBlue");
        double edgeDensity = features.get("edgeDensity");
        double textureUniformity = features.get("textureUniformity");

        // Example classification rules (simplified)
        // These are just for demonstration purposes, not accurate classification

        // Dark colors with high edge density might be electronic devices
        if (avgRed < 0.5 && avgGreen < 0.5 && avgBlue < 0.5 && edgeDensity > 0.1) {
            predictions.get(0).setProbability(0.6); // laptop
            predictions.get(4).setProbability(0.3); // smartphone
        }

        // Blue tones might suggest water bottles
        if (avgBlue > avgRed && avgBlue > avgGreen) {
            predictions.get(1).setProbability(0.7); // water bottle
        }

        // High uniformity might suggest solid objects like mugs
        if (textureUniformity > 0.1 && avgRed > 0.3) {
            predictions.get(2).setProbability(0.65); // coffee mug
        }

        // Medium brightness with texture might be books
        if (avgRed > 0.3 && avgRed < 0.7 && textureUniformity < 0.1) {
            predictions.get(3).setProbability(0.55); // book
        }

        // Override with filename-based simulated results for this demo
        if (filename.contains("laptop")) {
            predictions.get(0).setProbability(0.92); // laptop
            predictions.get(4).setProbability(0.05); // smartphone
        } else if (filename.contains("bottle") || filename.contains("water")) {
            predictions.get(1).setProbability(0.89); // water bottle
        } else if (filename.contains("mug") || filename.contains("coffee")) {
            predictions.get(2).setProbability(0.94); // coffee mug
        } else if (filename.contains("book")) {
            predictions.get(3).setProbability(0.91); // book
        } else if (filename.contains("phone") || filename.contains("smartphone")) {
            predictions.get(4).setProbability(0.95); // smartphone
            predictions.get(0).setProbability(0.03); // laptop
        }

        // Sort by probability
        predictions.sort((a, b) -> Double.compare(b.getProbability(), a.getProbability()));

        return predictions;
    }

    /**
     * Save the recognition results to a text file
     */
    private static void saveResultsToFile(String imageName, List<Prediction> results, Path outputDir)
            throws IOException {
        // Create a file named after the image
        String filename = imageName.substring(0, imageName.lastIndexOf('.')) + "_results.txt";
        Path outputFile = outputDir.resolve(filename);

        // Write the results to the file
        try (FileWriter writer = new FileWriter(outputFile.toFile())) {
            writer.write("Recognition results for: " + imageName + "\n");
            writer.write("-----------------------------------------\n");

            for (Prediction p : results) {
                writer.write(String.format("%-30s: %.2f%%\n",
                        p.getLabel(),
                        p.getProbability() * 100));
            }

            writer.write("\n\nImage Analysis:\n");
            writer.write("This is a simplified image analysis demo that shows how\n");
            writer.write("basic image processing can extract features like color,\n");
            writer.write("texture, and edges. In a real AI system, these features\n");
            writer.write("would be inputs to a machine learning model trained on\n");
            writer.write("thousands of product images.\n");
        }

        System.out.println("Results saved to file: " + outputFile);
    }

    /**
     * Save a processed version of the image
     */
    private static void saveProcessedImage(BufferedImage original, String imageName, Path outputDir) {
        try {
            // Create a copy of the image to draw on
            BufferedImage processed = new BufferedImage(
                    original.getWidth(),
                    original.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D g = processed.createGraphics();
            g.drawImage(original, 0, 0, null);

            // Add a simple border to show processing
            g.setColor(Color.GREEN);
            g.setStroke(new BasicStroke(5));
            g.drawRect(0, 0, processed.getWidth() - 1, processed.getHeight() - 1);

            // Add text showing it's been analyzed
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Analyzed", 20, 40);

            g.dispose();

            // Save the processed image
            String outputFilename = imageName.substring(0, imageName.lastIndexOf('.')) + "_processed.jpg";
            Path outputFile = outputDir.resolve(outputFilename);
            ImageIO.write(processed, "jpg", outputFile.toFile());

        } catch (IOException e) {
            System.err.println("Error saving processed image: " + e.getMessage());
        }
    }

    /**
     * A simple class to hold a prediction label and its probability
     */
    private static class Prediction {
        private String label;
        private double probability;

        public Prediction(String label, double probability) {
            this.label = label;
            this.probability = probability;
        }

        public String getLabel() {
            return label;
        }

        // public void setLabel(String label) {
        //     this.label = label;
        // }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }
    }
}
package com.kulkeez;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Product Image Recognition utility that demonstrates basic image processing techniques 
 * to analyze and classify product images.
 * A simplified lab for beginners to understand image processing concepts
 * without complex dependencies.
 */
public class ImageRecognitionUsingModel {

    /**
     * Main method to execute the image recognition process
     */
    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        // Path to your image file
        String imagePath = "pill_bottle.png";

        // Use Java NIO to read the image from the classpath 
        ClassLoader classLoader = ImageRecognitionUsingModel.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(imagePath);
        if (resourceUrl == null) {
            throw new IOException("Resource not found: " + imagePath);
        }

        // Use URL.openStream() to work with both JAR and classpath resources
        InputStream is = resourceUrl.openStream();
        // Load image
        Image img = ImageFactory.getInstance().fromInputStream(is);

        // Run prediction
        Classifications predictions = predict(img);

        // Print results
        System.out.println("Top 5 Predictions:");
        predictions.topK(5).forEach(System.out::println);
    }

    /**
     * Predicts the class of the given image using a pre-trained model from TorchHub.
     * The method defines a translator to preprocess the image and sets up criteria to load the model.
     * It then uses a predictor to get the classifications for the input image.
     *
     * @param image The input image to classify
     * @return Classifications containing the predicted classes and their probabilities
     * @throws IOException If there is an error loading the model or processing the image
     * @throws ModelException If there is an error with the model
     * @throws TranslateException If there is an error during translation of input/output
     */
    public static Classifications predict(Image image) throws IOException, ModelException, TranslateException {
        // Define translator (preprocessing)
        Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                .addTransform(new Resize(224, 224))
                .addTransform(new ToTensor())
                .optApplySoftmax(true)
                .build();

        // Updated criteria to use explicit model from TorchHub
        Criteria<Image, Classifications> criteria = Criteria.builder()
                .setTypes(Image.class, Classifications.class)
                .optTranslator(translator)
                .optArtifactId("resnet") // Use explicit model name
                .optEngine("PyTorch") // Specify engine
                .build();

        try (ZooModel<Image, Classifications> model = criteria.loadModel();
             Predictor<Image, Classifications> predictor = model.newPredictor()) {
            return predictor.predict(image);
        }
    }
}
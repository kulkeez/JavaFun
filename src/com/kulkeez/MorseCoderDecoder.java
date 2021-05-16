package com.kulkeez;


import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Program to convert English text into Morse code and vice versa. 
 * I was inspired to write this code after reading "Code - Charles Petzold"
 * 
 * @author Vikram Kulkarni
 *
 */
public class MorseCoderDecoder {
	
    private static String[] alphabets = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "0", " " };
    
    private static String[] dotties = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.",
            "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.",
            "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-",
            "-.--", "--..", ".----", "..---", "...--", "....-", ".....",
            "-....", "--...", "---..", "----.", "-----", "|" };

    private static final Logger logger = LoggerFactory.getLogger(MorseCoderDecoder.class);
    
    /**
     * 
     * @param args
     */
	public static void main(String[] args) {
	    Scanner input = new Scanner(System.in);

		// Set up a simple configuration that logs on the console.
    	BasicConfigurator.configure();
    	logger.debug("Launching the Morse Coder / Decoder program ...");
		
	    System.out.println("Enter 'E' to convert from English to Morse code or 'M' to convert from Morse code to English:");
	    String ans = input.nextLine();

	    if (("e").equalsIgnoreCase(ans)) {
	        System.out.println("Please enter the text you would like to convert to Morse Code: ");
	        String english = input.nextLine();
	        System.out.println("You entered the English text: " + english);
	        
	        char[] translateThis = (english.toLowerCase()).toCharArray();
	        System.out.println("Translated Morse code: " + MorseCoderDecoder.toMorse(translateThis)); 
	    }
	    else if ("m".equalsIgnoreCase(ans)) {
	        System.out.println("Please enter the Morse code you would like to convert to English (separate words with '|'):");
	        System.out.println("Example Morse Code Input: ....|.-|.--.|.--.|-.-- ");
	        String morseText = input.nextLine();
	        System.out.println("You entered the Morse Code: " + morseText);
	        
	        String[] morseTextMinusPipe = (morseText.split("[|]", 0));
	        System.out.println("Translating Morse: " + Arrays.toString(morseTextMinusPipe));

	        System.out.println("Morse code to English text: " + MorseCoderDecoder.toEnglish(morseTextMinusPipe));
	    }
	    else
	    	System.out.println("Invalid input, please try again.");
	    
	    input.close();
	 }

	
	/**
	 * Convert English phrase/sentence to Morse Code
	 * 
	 * @param translateThis
	 * @return Morse code for the input English sentence
	 */
	public static String toMorse(char[] translateEnglish) {            
		String morse = "";
		for (int j = 0; j < translateEnglish.length; j++) {
			char a = translateEnglish[j];
			if(Character.isLetter(a)) {
				morse += dotties[a - 'a'];
				morse += "  ";	// separate each morse letter with 2 spaces for readability
			}
		}
		
		logger.debug("Morse = " + morse);
		return morse;
	}

	
	/**
	 * 
	 * Convert Morse text into English
	 * 
	 * @param translateMorse
	 * @return
	 */
	public static String toEnglish(String[] translateMorse) {
		String english = "";

		for (String morseString : translateMorse) {
			logger.debug("Decoding Morse: {0}", morseString);
			
			// map dotties to alpha
			for (int i = 0; i < dotties.length; i++) {
				if(dotties[i].equals(morseString)) {
					logger.trace("Decoded to: " + dotties[i]);
					english += alphabets[i];
				}
	        }

		}
		logger.debug("English: " + english);
		return english;
	}
}

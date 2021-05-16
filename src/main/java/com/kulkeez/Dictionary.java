package com.kulkeez;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Dictionary of adjectives and nouns.
 * This dictionary can be used to pick a word and use it for simulating interesting names
 * 
 * @author Vikram Kulkarni
 *
 */
public class Dictionary {
	private List<String> nouns = new ArrayList<String>();
    private List<String> adjectives = new ArrayList<String>();

    private final int prime;
        
    private static final Logger logger = LoggerFactory.getLogger(Dictionary.class);
    
    /**
     * Default constructor that loads the resource files
     * 
     */
    public Dictionary() {
       	logger.debug("Loading Adjectives and Nouns resource files...");
        
       	loadDictionaryResources("a.txt", adjectives);
        loadDictionaryResources("n.txt", nouns);

        int combinedSize = size();
        int primeCombo = 2;
        
        while (primeCombo <= combinedSize) {
            int nextPrime = primeCombo + 1;
            primeCombo *= nextPrime;
        }
        prime = primeCombo + 1;
    }

	    
    /**
     * Total size of the combined words.
     */
    public int size() {
        return nouns.size() * adjectives.size();
    }

    
    /**
     * Sufficiently big prime that's bigger than {@link #size()}
     */
    public int getPrime() {
        return prime;
    }

    
    /**
     * Return a word from the dictionary
     * 
     * @param i
     * @return Word from the dictionary
     */
    public String getWord(int i) {
        int a = i % adjectives.size();
        int n = i / adjectives.size();

        return adjectives.get(a) + "_" + nouns.get(n);
    }

    
	/**
	 * Load the Adjectives and Nouns resource files
	 *   
	 * @param fileName
	 * @param col
	 * 
	 */
    private void loadDictionaryResources(String fileName, List<String> col) {
    	try {
			String line;
	    	logger.debug("Loading resource file: {}", fileName);

	    	InputStream is = getClass().getResourceAsStream(fileName);
			
    		if (is != null) {
    			BufferedReader resourceReader = new BufferedReader(
    					new InputStreamReader(is, "US-ASCII"));

    			logger.debug("Loaded Adjectives and Nouns into the Dictionary successfully!");

	    		while ((line = resourceReader.readLine()) != null) {
	    			//System.out.println("Line:" + line);
	    			col.add(line);
	    		}
    		}
    		else
    			logger.error("Class loader is unable to load Dictionary resource files!");
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    static final Dictionary INSTANCE = new Dictionary();

    /**
     * Quick unit test the functionality 
     * 
     * @param args
     */
    public static void main (String args[]) {
    	try {
    		// Set up a simple configuration that logs on the console.
        	BasicConfigurator.configure();
        	logger.debug("Launching the Dictionary ...");
        	
    		Dictionary d = new Dictionary();
    		logger.debug("Dictionary Size is: {}", d.size());
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
}

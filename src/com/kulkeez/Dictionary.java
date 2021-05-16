package com.kulkeez;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * Dictionary of adjectives and nouns.
 * This dictionary can be used to pick a word and use it for simulation
 * 
 * @author Vikram Kulkarni
 *
 */
public class Dictionary {
	private List<String> nouns = new ArrayList<String>();
    private List<String> adjectives = new ArrayList<String>();

    private final int prime;

    /**
     * Default constructor that loads the resource files
     * 
     */
    public Dictionary() {
        try {
        	System.out.println("Loading Adjectives and Nouns resource files...");
            load("a.txt", adjectives);
            load("n.txt", nouns);
            
            System.out.println("Loaded Adjectives and Nouns into the Dictionary successfully!");
        } 
        catch (IOException e) {
            throw new Error(e);
        }

        int combo = size();
        int primeCombo = 2;
        
        while (primeCombo <= combo) {
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
    public String word(int i) {
        int a = i % adjectives.size();
        int n = i / adjectives.size();

        return adjectives.get(a) + "_" + nouns.get(n);
    }

    
	/**
	 * Load the Adjectives and Nouns resource files
	 *   
	 * @param fileName
	 * @param col
	 * @throws IOException
	 */
    private void load(String fileName, List<String> col) throws IOException {
    	System.out.println("Loading resource file: " + fileName);
    	try {
    		BufferedReader resourceReader = new BufferedReader(
    			new InputStreamReader(getClass().getResourceAsStream(fileName), "US-ASCII"));
    		String line;
        
    		while ((line = resourceReader.readLine()) != null) {
    			//System.out.println("Line:" + line);
    			col.add(line);
    		}
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
    	Dictionary d = new Dictionary();
    	System.out.println("Dictionary Size is:" + d.size());
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
}

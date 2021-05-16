package com.kulkeez;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Generates pseudo random unique names that combines one adjective and one noun,
 * like "friendly tiger" or "good apple".
 * 
 * There are about 1.5 million unique combinations, and if you keep requesting a new word
 * it will start to loop (but this code will generate all unique combinations before it starts
 * to loop.)
 * 
 * @author Vikram Kulkarni
 *  
 */
public class RandomNameGenerator {

	private static final Logger logger = LoggerFactory.getLogger(RandomNameGenerator.class);

	private int pos;

	/**
	 * 
	 * @param seed
	 */
    public RandomNameGenerator(int seed) {
        this.pos = seed;
    }

    /**
     * 
     */
    public RandomNameGenerator() {
        this((int) System.currentTimeMillis());
    } 
    
    /**
     * 
     * @return
     */
    public synchronized String next() {
        Dictionary dictionary = Dictionary.INSTANCE;
        pos = Math.abs(pos + dictionary.getPrime()) % dictionary.size();
        
        logger.debug("Picking this random position within the Dictionary: {}", pos);
        
        return dictionary.getWord(pos);
    }

    
    /**
     * For quick unit testing
     * 
     * @param args
     */
    public static void main (String args[]) {
    	try {
			// Set up a simple configuration that logs on the console.
	    	BasicConfigurator.configure();
	    	logger.debug("Launching the Random Name Generator program ...");
	
	    	RandomNameGenerator namesGenerator = new RandomNameGenerator(0);
	    	
	    	int sz = Dictionary.INSTANCE.size();
	        Set<String> s = new HashSet<String>(sz);
	
	        logger.debug("Generating 10 random names...");
	        
	        for (int i=0; i<10; i++)
	        	logger.debug(namesGenerator.next());
	    }
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}

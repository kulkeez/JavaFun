package com.kulkeez;

import java.util.HashSet;
import java.util.Set;

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
        Dictionary d = Dictionary.INSTANCE;
        pos = Math.abs(pos + d.getPrime()) % d.size();
        
        //System.out.println("Picking random Position in Dictonary: " + pos);
        
        return d.word(pos);
    }

    
    /**
     * For quick unit testing
     * 
     * @param args
     */
    public static void main (String args[]) {
    	RandomNameGenerator namesGenerator = new RandomNameGenerator(0);
    	
    	int sz = Dictionary.INSTANCE.size();
        Set<String> s = new HashSet<String>(sz);

        for (int i=0; i<10; i++)
        	System.out.println(namesGenerator.next());
    }
}

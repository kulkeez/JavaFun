package com.kulkeez;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Interesting blog on finalize() 
 * Reference: https://vanillajava.blogspot.com/2012/12/object-resurrection.html
 * 
 * @author kulkarvi
 *
 */
public class Zombies {

	private static final List ZOMBIES = new ArrayList<>();
	
	private int num;
	
	private static final Logger logger = LoggerFactory.getLogger(Zombies.class);
	
	/*
	 * constructor
	 * 
	 */
    public Zombies(int num) {
         this.num = num;
    }
    
    
    /**
     * 
     * @param args
     * @throws InterruptedException
     */
	public static void main(String[] args) throws InterruptedException {
		
		// Set up a simple configuration that logs on the console.
    	BasicConfigurator.configure();
    	logger.debug("Launching the Zombies ATTACK !!!!");
    	
		for (int i = 0; i < 3; i++)
            ZOMBIES.add(new Zombies(i));
        
		for (int j = 0; j < 5; j++) {
            logger.debug("Zombies: " + ZOMBIES);
            
            ZOMBIES.clear();
            System.gc();
            Thread.sleep(100);
        }
	}

	
	@Override
    protected void finalize() throws Throwable {
        logger.debug("Resurrecting Zombie#" + num);
        ZOMBIES.add(this);
    }
	
	
    @Override
    public String toString() {
        return "Zombie#"+ num;
    }
}

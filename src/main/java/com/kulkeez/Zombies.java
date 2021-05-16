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

	private static final List<Zombies> ZOMBIES = new ArrayList<>();
	
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
    	logger.info("Launching the Zombies ATTACK !!!!");
    	
		for (int i = 1; i <= 3; i++)
            ZOMBIES.add(new Zombies(i));
        
		for (int j = 1; j <= 5; j++) {
            logger.info("Zombies: {}", ZOMBIES);
            
            ZOMBIES.clear();
            System.gc();
            logger.debug("Forcefully called the Garbage collector {} time(s)", j);
            
            logger.debug("Sleeping 1 second...");
            Thread.sleep(1000);
        }
		logger.info("Observe the output above: finalize() is only called ONCE! Therefore, the ZOMBIES are resurrected only once.");
		logger.info("SUMMARY: Avoid using finalize(). It will only be called once if the object is resurrected.");
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

package com.kulkeez;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * Singleton Utility class to read a properties file 'kulkeez.properties' included in the 
 * application classpath
 * 
 * @author Vikram Kulkarni
 *
 */
public class PropertiesUtil {
	
	private Properties props 				= null;
	private static PropertiesUtil propUtil 	= null; 
	
	private static Logger logger = Logger.getLogger(PropertiesUtil.class.getName());
	
	private PropertiesUtil() {
		InputStream is = PropertiesUtil.class.getResourceAsStream("/kulkeez.properties");
		try {
		    try {
		    	props = new Properties();
				props.load(is);
				logger.info("Loaded kulkeez.properties file.");
			} 
		    catch (IOException e) {
				e.printStackTrace();
			}
		}
		finally {
		    try {
		        is.close();
		    }
		    catch (Exception e) {
		        // ignore this exception
		    }
		}
	}
	
	public static PropertiesUtil getInstance() {
		if(propUtil == null) {
			propUtil = new PropertiesUtil();
		}
		return propUtil;
	}

	public String getProperty(String name)  {
		return props.getProperty(name);
	}
}

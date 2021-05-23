package com.kulkeez;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * 
 * A simple Swing-based application.
 * 
 * @author kulkarvi
 *
 */
public class HelloSwing implements ActionListener {

	JTextField jtfFirst;  // holds the first file name
	JTextField jtfSecond; // holds the second file name

	JButton jbtnComp; // button to compare the files

	JLabel jlabFirst, jlabSecond; // displays prompts
	JLabel jlabResult; // displays results and error messages

	public void launch() {
	    // Create a new JFrame container.
	    JFrame jFrame = new JFrame("Compare Files");
	
	    // Specify FlowLayout for the layout manager.
	    jFrame.setLayout(new FlowLayout());
	
	    // Terminate the program when the user closes the application.
	    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    // Create the text fields for the file names.
	    jtfFirst = new JTextField(14);
	    jtfSecond = new JTextField(14);
	
	    // Set the action commands for the text fields.
	    jtfFirst.setActionCommand("fileA");
	    jtfSecond.setActionCommand("fileB");
	
	    // Create the Compare button.
	    JButton jbtnComp = new JButton("Compare");
	
	    // Add action listener for the Compare button.
	    jbtnComp.addActionListener(this);
	    jbtnComp.addActionListener(e -> System.out.println(e));
	    
	    // Use ALT+C to press the button
	    jbtnComp.setMnemonic('c');
	    
	    // Create the labels.
	    jlabFirst = new JLabel("First file: ");
	    jlabSecond = new JLabel("Second file: ");
	    jlabResult = new JLabel("");
	
	    // Add the components to the content pane.
	    jFrame.add(jlabFirst);
	    jFrame.add(jtfFirst);
	    jFrame.add(jlabSecond);
	    jFrame.add(jtfSecond);
	    jFrame.add(jbtnComp);
	    jFrame.add(jlabResult);
	
	    // Give the frame an initial size.
	    //jfrm.setSize(200, 190);
	    jFrame.pack();
	    
	    // Position the frame at the center
	    jFrame.setLocationRelativeTo(null);
	    
	    // Position the frame using the native OS windowing system 
	    //jFrame.setLocationByPlatform(true);
	    
	    // Display the frame.
	    jFrame.setVisible(true);
	}
	

  	// Compare the files when the Compare button is pressed.
  	public void actionPerformed(ActionEvent ae) {
  		int i=0, j=0;

  		// First, confirm that both file names have
	    // been entered.
	    if(jtfFirst.getText().equals("")) {
	    	jlabResult.setText("First file name missing.");
	    	return;
	    }
	    
	    if(jtfSecond.getText().equals("")) {
	    	jlabResult.setText("Second file name missing.");
	    	return;
	    }
	    
	    // Compare files - use try-with-resources to manage the files.
	    try (FileInputStream f1 = new FileInputStream(jtfFirst.getText());
	    		FileInputStream f2 = new FileInputStream(jtfSecond.getText())) {
	    	// Check the contents of each file.
	    	do {
	    		i = f1.read();
	    		j = f2.read();
	    		
	    		if(i != j) 
	    			break;
	    		
	    	} while(i != -1 && j != -1);

	    	if(i != j)
	    		jlabResult.setText("Files are not the same.");
	    	else
	    		jlabResult.setText("Files are same.");
	    } 
	    catch(IOException exc) {
	    	jlabResult.setText("File Error");
	    }
  	}

  	
  	/**
  	 * 
  	 * @param args
  	 */
	public static void main(String args[]) {
		// Create the frame on the event dispatching thread (Java 8 Lambda)
		SwingUtilities.invokeLater( () -> new HelloSwing().launch());
		
		// Use this if using version < Java 8
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SwingFC();
			}
		});  */
	}
}
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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * A simple Swing-based application to render a JTree using a DOM
 * 
 * @author kulkarvi
 *
 */
public class SimpleJTreeUsingDom  {


	public void launch() {
	    // Create a new JFrame container.
	    JFrame jFrame = new JFrame("JTree using a DOM");
	
	    // Give the frame an initial size.
	    jFrame.setSize(300, 500);
	    //jFrame.pack();
	    
	    // Terminate the program when the user closes the application.
	    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel pan = new JPanel(new GridLayout(1, 1));
	    XmlJTree myTree = new XmlJTree(null);
	    
	    // Add the components to the content pane.
	    jFrame.add(new JScrollPane(myTree));
	    
	    JButton button = new JButton("Choose file");
	    button.addActionListener(e -> {
	    	JFileChooser chooser = new JFileChooser();
	    	FileNameExtensionFilter filter = new FileNameExtensionFilter("XML file",
	    			"xml");
	    	chooser.setFileFilter(filter);
	    	
	    	int returnVal = chooser.showOpenDialog(null);
	    	
	    	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    		myTree.setPath(chooser.getSelectedFile().getAbsolutePath());
	    	}
	    });
	    
	    // Add the component to the content pane.
	    pan.add(button);
	    
	    jFrame.add(pan, BorderLayout.SOUTH);
	    
	    button.addActionListener(e -> System.out.println(e));
	    
	    // Use ALT+C to press the button
	    button.setMnemonic('c');
	    
	    // Position the frame at the center
	    jFrame.setLocationRelativeTo(null);
	    
	    // Position the frame using the native OS windowing system 
	    //jFrame.setLocationByPlatform(true);
	    
	    // Display the frame.
	    jFrame.setVisible(true);
	}
	
  	
  	/**
  	 * 
  	 * @param args
  	 */
	public static void main(String args[]) {
		// Create the frame on the event dispatching thread (Java 8 Lambda)
		SwingUtilities.invokeLater( () -> new SimpleJTreeUsingDom().launch());
	}
	
}


class XmlJTree extends JTree {

	DefaultTreeModel dtModel = null;

	public XmlJTree(String filePath) {
		if (filePath != null)
			setPath(filePath);
	}

	public void setPath(String filePath) {
		Node root = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			root = (Node) doc.getDocumentElement();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Can't parse file", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (root != null) {
			dtModel = new DefaultTreeModel(builtTreeNode(root));
			this.setModel(dtModel);
		}
	}

	private DefaultMutableTreeNode builtTreeNode(Node root) {
		DefaultMutableTreeNode dmtNode;

		dmtNode = new DefaultMutableTreeNode(root.getNodeName());
		NodeList nodeList = root.getChildNodes();
		
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);

			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.hasChildNodes()) {
					dmtNode.add(builtTreeNode(tempNode));
				}
			}
		}
		return dmtNode;
	}
}

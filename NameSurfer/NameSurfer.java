/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import acmx.export.javax.swing.JTextPane;

import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
	
	// instance variables
	
	private JButton graphButton , clearButton;
	private JTextField nameField ;
	private JLabel nameLabel;
	
	private NameSurferDataBase dataBase = new NameSurferDataBase(NAMES_DATA_FILE);
	
	private NameSurferEntry currentEntry;
	
	private NameSurferGraph graph = new NameSurferGraph(); 


/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		add(graph);
		
		nameLabel = new JLabel("Name");
		add(nameLabel , SOUTH);
		
		nameField = new JTextField(20);
		add(nameField  , SOUTH);
		nameField .addActionListener(this);
		
		graphButton = new JButton("Graph");
		add(graphButton , SOUTH);
		graphButton.addActionListener(this);
		
		clearButton = new JButton("Clear");
		add(clearButton , SOUTH);
		clearButton.addActionListener(this);
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Graph") || e.getSource() == nameField) {
			if (!nameField .getText().equals("")) {
				currentEntry = dataBase.findEntry(nameField .getText());
				if (currentEntry != null) graph.addEntry(currentEntry);
				
				nameField .setText("");
			}
		} else if (e.getActionCommand().equals("Clear")) {
			graph.clear();
		}
	}
}

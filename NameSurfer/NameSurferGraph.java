/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {
	
	// instance variables
	private ArrayList<NameSurferEntry> entries = new ArrayList<NameSurferEntry>();
	private Font myFont = new Font("Arial" , Font.PLAIN , new GLabel("").getFont().getSize());
	private Color[] colorArray = {Color.black, Color.red, Color.blue, Color.cyan, Color.green, Color.DARK_GRAY, Color.magenta};
	
	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
	}
	
	private void addVerticalLines() { // draws vertical lines to separate decades
		for(int i = 0; i < NDECADES - 1; i++) {
			add(new GLine( (i + 1) * getWidth()/NDECADES , 0 ,  (i + 1) * getWidth()/NDECADES , getHeight()));
		}
	}
	
	private void addHorizontalLines() { // draws two horizontal lines
		add(new GLine(0 , GRAPH_MARGIN_SIZE , getWidth() , GRAPH_MARGIN_SIZE));
		add(new GLine(0 , getHeight() - GRAPH_MARGIN_SIZE, getWidth() , getHeight() - GRAPH_MARGIN_SIZE));
	}
	
	private void addDecadeLabels() { // displays the decades
		for(int i = 0; i < NDECADES; i++) {
			add(new GLabel("" + (START_DECADE + 10 * i)) , 2 + i * getWidth()/NDECADES , getHeight() - GRAPH_MARGIN_SIZE + myFont.getSize() * 3/2);
		}
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		//	 You fill this in //
		entries.clear();
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		// You fill this in //
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i) == entry) {
				update();
			}
		}
		entries.add(entry);
		update();
	}
	
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		//	 You fill this in //
		removeAll();
		addVerticalLines();
		addHorizontalLines();
		addDecadeLabels();
		drawEntries();
	}

	private void drawEntries() { // adds labels and graph lines for the entries, calls addGraphsForRankZero and addGraphForTop1000 methods
		for (int i = 0; i < entries.size(); i++) {
			Color color = generateColor(i); // generates the which will be used for labels and lines
			for(int j = 0; j < NDECADES; j++) {
				if (entries.get(i) != null) {
					GLabel nameLabel = new GLabel(entries.get(i).getName());
					GLine line = null;
					
					nameLabel.setFont(myFont);
					nameLabel.setColor(color);
					
					double convert = (double)(getHeight() - 2 * GRAPH_MARGIN_SIZE)/MAX_RANK; // used for placing lines and labels at corresponding places
					
					if (entries.get(i).getRank(j) == 0) {
						addGraphsForRankZero(nameLabel , line , convert, i , j , color);
					} else {
						addGraphsForRankTop1000(nameLabel , line , convert , i , j , color);
					}
				}
			}
		}
	}
	
	// adds graph labels and lines for the 0 ranked decades for each entry
	private void addGraphsForRankZero(GLabel nameLabel , GLine line , double convert , int i , int j , Color color) {
		nameLabel.setLabel(nameLabel.getLabel() + " *");
		double yCor = getHeight() - GRAPH_MARGIN_SIZE - new GLabel("").getDescent();
		
		
		add(nameLabel ,  2 + j * getWidth()/NDECADES , yCor);
		
		drawGraphLine(line , yCor + new GLabel("").getDescent(), convert , i , j , color);
	}
	
	// adds graph labels and lines for the decades in which the entries didn't have a 0 rank
	private void addGraphsForRankTop1000(GLabel nameLabel , GLine line , double convert , int i , int j , Color color) {
		nameLabel.setLabel(nameLabel.getLabel() + " " + entries.get(i).getRank(j));
		double yCor = GRAPH_MARGIN_SIZE + (entries.get(i).getRank(j) * convert);
				
		add(nameLabel ,  2 + j * getWidth()/NDECADES , yCor);
		
		drawGraphLine(line , yCor , convert , i , j , color);
	}
	
	private void drawGraphLine(GLine line , double yCor , double convert , int i , int j , Color color) { // draws a single graph line
		if (j != NDECADES - 1) { // checking if the graph is at the last decade
			line = new GLine(j * getWidth()/NDECADES , yCor, (j + 1) * getWidth()/NDECADES ,
					entries.get(i).getRank(j + 1) > 0 ? GRAPH_MARGIN_SIZE + (entries.get(i).getRank(j + 1) * convert) : getHeight() - GRAPH_MARGIN_SIZE);
			line.setColor(color);
			add(line);
		}
	}
	
	private Color generateColor(int i) { // generates colour for each entry
		if (i > colorArray.length - 1) {
			while(i > colorArray.length - 1) {
				i -= colorArray.length;
			}
		}
		return colorArray[i];
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}

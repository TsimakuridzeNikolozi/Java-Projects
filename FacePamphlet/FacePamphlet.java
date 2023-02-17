/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {
	
	// instance variables
	private JTextField nameTextField , statusTextField , pictureTextField , friendTextField;
	private JButton addButton , deleteButton , lookupButton , statusChangeButton , pictureChangeButton , friendAddButton;
	private JLabel nameLabel = new JLabel("Name");
	
	private FacePamphletDatabase dataBase = new FacePamphletDatabase();
	
	private FacePamphletProfile currentProfile;
	
	private FacePamphletCanvas canvas;

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		canvas = new FacePamphletCanvas();
		add(canvas);
		
		initializeInteractors();
		
		addActionListenersToInteractors();
		
		addInteractorsOnTheWindow();
		
		addActionListeners();
    }
	
	private void initializeInteractors() { // initializes all the interactors
		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		statusTextField = new JTextField(TEXT_FIELD_SIZE);
		pictureTextField = new JTextField(TEXT_FIELD_SIZE);
		friendTextField = new JTextField(TEXT_FIELD_SIZE);
		
		addButton = new JButton("Add");
		deleteButton = new JButton("Delete");
		lookupButton = new JButton("Lookup");
		statusChangeButton = new JButton("Change Status");
		pictureChangeButton = new JButton("Change Photo");
		friendAddButton = new JButton("Add Friend");
	}
	
	private void addActionListenersToInteractors() { // adds action listeners to the textFields
		nameTextField.addActionListener(this);
		statusTextField.addActionListener(this);
		pictureTextField.addActionListener(this);
		friendTextField.addActionListener(this);
	}
	
	private void addInteractorsOnTheWindow() { // adds all the interactors on the display
		add(nameLabel , NORTH);
		add(nameTextField , NORTH);
		add(addButton , NORTH);
		add(deleteButton , NORTH);
		add(lookupButton , NORTH);
		
		add(statusTextField , WEST);
		add(statusChangeButton , WEST);
		add(new JLabel(EMPTY_LABEL_TEXT) , WEST);
		add(pictureTextField , WEST);
		add(pictureChangeButton , WEST);
		add(new JLabel(EMPTY_LABEL_TEXT) , WEST);
		add(friendTextField , WEST);
		add(friendAddButton , WEST);
	}
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
    	String entry;
		if (e.getSource() == addButton) { // if the user clicks "Add" button
			entry = nameTextField.getText();
			if (!entry.equals("")) {
				addProfile(entry);
			}
		} else if (e.getSource() == deleteButton) { // if the user clicks "Delete" button
			entry = nameTextField.getText();
			if (!entry.equals("")) {
				deleteProfile(entry);
			}
		} else if (e.getSource() == lookupButton) { // if the user clicks "Lookup" button
			entry = nameTextField.getText();
			if (!entry.equals("")) {
				lookupProfile(entry);
			}
		} else if (e.getSource() == statusTextField || e.getSource() == statusChangeButton) { // if the user clicks "Change Status" button or presses "Enter"
			entry = statusTextField.getText();
			if (!entry.equals("")) {
				changeStatus(entry);
			}
		} else if (e.getSource() == pictureTextField || e.getSource() == pictureChangeButton) { // if the user clicks "Change Picture" button or presses "Enter"
			entry = pictureTextField.getText();
			if (!entry.equals("")) {
				changePicture(entry);
			}
		} else if (e.getSource() == friendTextField || e.getSource() == friendAddButton) { // if the user clicks "Add Friend" button or presses "Enter"
			entry = friendTextField.getText();
			if (!entry.equals("")) {
				addFriend(entry);
			}
		}
	}
    
    private void addProfile(String entry) { // what to do when user clicks "Add" button
    	canvas.removeAll();
    	if (dataBase.containsProfile(entry)){
			canvas.showMessage("Profile with the name " + entry + " already exists");
		} else {
			dataBase.addProfile(new FacePamphletProfile(entry));
			canvas.showMessage("New profile created");
		}
    	currentProfile = dataBase.getProfile(entry);
    	canvas.displayProfile(currentProfile);
		nameTextField.setText("");
    }
    
    private void deleteProfile(String entry) { // what to do when user clicks "delete" button
    	canvas.removeAll();
    	if (dataBase.containsProfile(entry)){
    		Iterator <String> friendsIterator = dataBase.getProfile(entry).getFriends();
    		if (friendsIterator != null) {
    			while(friendsIterator.hasNext()) {
        			dataBase.getProfile(friendsIterator.next()).removeFriend(entry); // removing the entry from the friend lists of its friends
        		}
    		}
			dataBase.deleteProfile(entry);
			canvas.showMessage("Profile of " + entry + " deleted");
		} else {
			canvas.showMessage("A profile with the name " + entry + " doesn't exist");
		}
    	currentProfile = null;
    	canvas.displayProfile(currentProfile);
		nameTextField.setText("");
    }
    
    private void lookupProfile(String entry) { // what to do when user clicks "Lookup" button
    	canvas.removeAll();
    	if (dataBase.containsProfile(entry)){
    		canvas.showMessage("Displaying " + entry);
			currentProfile = dataBase.getProfile(entry);
		} else {
			canvas.showMessage("A profile with the name " + entry + " doesn't exist");
			currentProfile = null;
		}
    	canvas.displayProfile(currentProfile);
		nameTextField.setText("");
    }
    
    private void changeStatus(String entry) { // what to do when user clicks "Change Status" button or presses "Enter"
    	canvas.removeAll();
    	if (currentProfile != null) {
    		dataBase.getProfile(currentProfile.getName()).setStatus(entry);
    		currentProfile = dataBase.getProfile(currentProfile.getName());
    		canvas.showMessage("Status updated to " + entry);
    	} else {
    		canvas.showMessage("Please select a profile to change status");
    	}
    	canvas.displayProfile(currentProfile);
    	statusTextField.setText("");
    }
    
    private void changePicture(String entry) { // what to do when user clicks "Change Picture" button or presses "Enter"
    	canvas.removeAll();
    	if (currentProfile != null) {
    		GImage image = null; 
        	try { 
        		image = new GImage(entry);
        		dataBase.getProfile(currentProfile.getName()).setImage(image);
        		currentProfile = dataBase.getProfile(currentProfile.getName());
        		canvas.showMessage("Picture updated");
        	} catch (ErrorException ex) { 
        		canvas.showMessage("Unable to open image file: " + entry);
        	}
    	} else {
    		canvas.showMessage("Please select a profile to change picture");
    	}
    	canvas.displayProfile(currentProfile);
    	pictureTextField.setText("");
    }
    
    private void addFriend(String entry) { // what to do when user clicks "Add Friend" button or presses "Enter"
    	canvas.removeAll();
    	if (currentProfile != null) { // if there is a current profile
    		if (dataBase.containsProfile(entry)) { // if the profile with the entered name exists
    			if (!entry.equals(currentProfile.getName())) { // if the entry isn't the same as the current profile
	    			boolean check = true;
	    			if (currentProfile.getFriends() != null) {
	    				Iterator <String> friendsIterator = currentProfile.getFriends();
	    				while (friendsIterator.hasNext()) { // checking if the friend with the entered name already exists
	            			if (friendsIterator.next().equals(entry)) {
	            				canvas.showMessage(currentProfile.getName() + " already has a friend named " + entry);
	            				check = false;
	            				break;
	            			}
	            		} 
	    			}
	    			if (check) { // adding a new friend to both profiles if they aren't friends
	    				dataBase.getProfile(currentProfile.getName()).addFriend(entry);
	    				dataBase.getProfile(entry).addFriend(currentProfile.getName());
	    				currentProfile.addFriend(entry);
	    				canvas.showMessage(entry + " added as a friend ");
	    			}
    			} else {
    				canvas.showMessage("You can't add your own profile as a friend");
    			}
    		} else { // if the profile with the entered name doesn't exist
    			canvas.showMessage(entry + " does not exist");
    		}
    	} else {
    		canvas.showMessage("Please select a profile to add friend");
    	}
    	canvas.displayProfile(currentProfile);
    	friendTextField.setText("");
    }

}

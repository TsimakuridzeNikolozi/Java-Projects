/* 
 * File: FacePamphletExtension.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.*;

public class FacePamphletExtension extends Program 
					implements FacePamphletConstants {
	
	// instance variables
	private JTextField nameTextField , statusTextField , pictureTextField , friendTextField;
	private JButton addButton , deleteButton , lookupButton , statusChangeButton , pictureChangeButton , friendAddButton ,
					logInButton , logOutButton , homeButton , friendRemoveButton , friendSuggestionButton;
	private JLabel nameLabel = new JLabel("Name");
	
	private FacePamphletDatabaseExtension dataBase = new FacePamphletDatabaseExtension();
	
	private FacePamphletProfileExtension currentProfile;
	
	private FacePamphletCanvasExtension canvas;
	
	private int numberOfTimesFriendSuggestionWasUsed = 0;

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		canvas = new FacePamphletCanvasExtension();
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
		friendRemoveButton = new JButton("Remove Friend");
		logInButton = new JButton("Log in");
		logOutButton = new JButton("Log out");
		homeButton = new JButton("Home");
		friendSuggestionButton = new JButton("Friend Suggestion");
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
		add(lookupButton , NORTH);
		add(logInButton , NORTH);
		add(new JLabel(EMPTY_LABEL_TEXT) , NORTH);
		add(deleteButton , NORTH);
		add(homeButton , NORTH); homeButton.setVisible(false);
		add(logOutButton , NORTH); logOutButton.setVisible(false);
		
		add(statusTextField , WEST);
		add(statusChangeButton , WEST);
		add(new JLabel(EMPTY_LABEL_TEXT) , WEST);
		add(pictureTextField , WEST);
		add(pictureChangeButton , WEST);
		add(new JLabel(EMPTY_LABEL_TEXT) , WEST);
		add(friendTextField , WEST);
		add(friendAddButton , WEST);
		add(friendRemoveButton , WEST);
		add(friendSuggestionButton , WEST);
		
		makeSomeInteractorsVisibleOrInvisible("Invisible");
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
			deleteProfile();
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
		} else if (e.getSource() == friendAddButton) { // if the user clicks "Add Friend" button or presses "Enter"
			entry = friendTextField.getText();
			if (!entry.equals("")) {
				addFriend(entry);
			}
		} else if (e.getSource() == logInButton) {
			entry = nameTextField.getText();
			if(!entry.equals("")) {
				logIn(entry);
			}
			nameTextField.setText("");
		} else if (e.getSource() == homeButton) {
			canvas.removeAll();
			canvas.displayProfile(currentProfile);
			makeSomeInteractorsVisibleOrInvisible("Visible");
			canvas.showMessage("Home page");
			homeButton.setVisible(false);
			logInButton.setVisible(false);
		} else if (e.getSource() == logOutButton) {
			canvas.removeAll();
			canvas.showMessage("Logged out of " + currentProfile.getName());
			currentProfile = null;
			makeSomeInteractorsVisibleOrInvisible("Invisible");
			addButton.setVisible(true);
			logInButton.setVisible(true);
		} else if (e.getSource() == friendRemoveButton) {
			entry = friendTextField.getText();
			if (!entry.equals("")) {
				removeFriend(entry);
			}
			friendTextField.setText("");
		} else if (e.getSource() == friendSuggestionButton) {
			generateFriendSuggestion();
		}
		dataBase.writeInfoInDataFile();
	}
    
    private void addProfile(String entry) { // what to do when user clicks "Add" button
    	canvas.removeAll();
    	if (dataBase.containsProfile(entry)){
			canvas.showMessage("Profile with the name " + entry + " already exists");
		} else {
			dataBase.addProfile(new FacePamphletProfileExtension(entry));
			canvas.showMessage("New profile created");
			currentProfile = dataBase.getProfile(entry);
	    	canvas.displayProfile(currentProfile);
			nameTextField.setText("");
			makeSomeInteractorsVisibleOrInvisible("Visible");
			addButton.setVisible(false);
			logInButton.setVisible(false);
			numberOfTimesFriendSuggestionWasUsed = 0;
		}
    }
    
    private void deleteProfile() { // what to do when user clicks "delete" button
    	canvas.removeAll();
    	Iterator <String> friendsIterator = dataBase.getProfile(currentProfile.getName()).getFriends();
		if (friendsIterator != null) {
			while(friendsIterator.hasNext()) {
    			dataBase.getProfile(friendsIterator.next()).removeFriend(currentProfile.getName()); // removing the entry from the friend lists of its friends
    		}
		}
		dataBase.deleteProfile(currentProfile.getName());
		canvas.showMessage("Profile of " + currentProfile.getName() + " deleted");
    	currentProfile = null;
    	makeSomeInteractorsVisibleOrInvisible("Invisible");
    	addButton.setVisible(true);
    	logInButton.setVisible(true);
		nameTextField.setText("");
    }
    
    private void lookupProfile(String entry) { // what to do when user clicks "Lookup" button
    	canvas.removeAll();
    	if (dataBase.containsProfile(entry)){
    		canvas.showMessage("Displaying " + entry);
    		canvas.displayProfile(dataBase.getProfile(entry));
    		makeSomeInteractorsVisibleOrInvisible("Invisible");
    		if (currentProfile != null) {
    			homeButton.setVisible(true);
    		}
		} else {
			canvas.showMessage("A profile with the name " + entry + " doesn't exist");
		}
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
        		dataBase.getProfile(currentProfile.getName()).setImageName(entry);
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
    
    private void logIn(String entry) { // what happens when an user clicks the logIn button
    	if (dataBase.containsProfile(entry)) {
    		if (currentProfile == null) {
    			currentProfile = dataBase.getProfile(entry);
    			canvas.displayProfile(currentProfile);
    			makeSomeInteractorsVisibleOrInvisible("Visible");
    			canvas.showMessage("Welcome back " + entry);
    		} else {
    			if (entry.equals(currentProfile.getName())) {
        			canvas.showMessage("You are already logged into this accaunt");
        		} else {
        			currentProfile = dataBase.getProfile(entry);
        			canvas.displayProfile(currentProfile);
        			makeSomeInteractorsVisibleOrInvisible("Visible");
        			canvas.showMessage("Welcome back " + entry);
        		}
    		}
    		addButton.setVisible(false);
    		logInButton.setVisible(false);
    		numberOfTimesFriendSuggestionWasUsed = 0;
    	} else {
    		canvas.showMessage("Profile with the name " + entry + " doesn't exist");
    	}
    }
    
    private void makeSomeInteractorsVisibleOrInvisible(String command) { // makes some interactors visible or invisible depending on what's on the display
    	boolean bool;
    	if (command.equals("Visible")) {
    		bool = true;
    	} else {
    		bool = false;
    	}
    	statusTextField.setVisible(bool);
    	statusChangeButton.setVisible(bool);
    	pictureTextField.setVisible(bool);
    	pictureChangeButton.setVisible(bool);
    	friendTextField.setVisible(bool);
    	friendAddButton.setVisible(bool);
    	friendRemoveButton.setVisible(bool);
    	friendSuggestionButton.setVisible(bool);
    	deleteButton.setVisible(bool);
    	logOutButton.setVisible(bool);
    }
    
    private void removeFriend(String entry) { // removes a friend 
    	boolean check = true;
    	Iterator <String> friendsIterator = dataBase.getProfile(currentProfile.getName()).getFriends();
		if (friendsIterator != null) {
			while(friendsIterator.hasNext()) {
				if (entry.equals(friendsIterator.next())) {
					dataBase.getProfile(currentProfile.getName()).removeFriend(entry);
					currentProfile.removeFriend(entry);
					dataBase.getProfile(entry).removeFriend(currentProfile.getName()); // removing the entry from the friend lists of its friends
					check = false;
					canvas.removeAll();
					canvas.displayProfile(currentProfile);
					canvas.showMessage(entry + " removed from your friend list");
					break;
				}
    		}
		}
		if (check) {
			canvas.showMessage("You don't have a friend named " + entry);
		}
    }
    
    private void generateFriendSuggestion() { // generates a friend suggestion based on mutual friends
    	Iterator<String> currentProfileIterator = currentProfile.getFriends();
    	ArrayList<String> currentProfileFriends = new ArrayList<String>();
    	while(currentProfileIterator.hasNext()) {
    		currentProfileFriends.add(currentProfileIterator.next());
    	}
    	boolean checkcheck = true;
    	for(int i = 0; i < currentProfileFriends.size(); i++) {
    		Iterator<String> friendsIterator = dataBase.getProfile(currentProfileFriends.get(i)).getFriends();
    		while(friendsIterator.hasNext()) {
    			String friendOfAFriend = friendsIterator.next();
    			boolean check = true;
    			for(int j = 0; j < currentProfileFriends.size(); j++) {
    				if (friendOfAFriend.equals(currentProfileFriends.get(j)) || friendOfAFriend.equals(currentProfile.getName())) {
    					check = false;
    				}
    			}
    			if (check) {
    				canvas.showMessage("Friend Suggestion: " + friendOfAFriend);
    				checkcheck = false;
    			}
    		}
    	}
    	if (checkcheck) {
    		canvas.showMessage("No Friend Suggestions ");
    	}
    }

}

/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	// instance variables
	private GLabel messageLabel , nameLabel , imageLabel , statusLabel , friendsLabel;
	private GImage image;
	private GRect emptyImage;
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		messageLabel = new GLabel("");
		nameLabel = new GLabel("");
		imageLabel = new GLabel("");
		statusLabel = new GLabel("");
		friendsLabel = new GLabel("");
		messageLabel.setFont(MESSAGE_FONT);
		nameLabel.setFont(PROFILE_NAME_FONT);
		imageLabel.setFont(PROFILE_IMAGE_FONT);
		statusLabel.setFont(PROFILE_STATUS_FONT);
		friendsLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		messageLabel.setLabel(msg);
		add(messageLabel , getWidth()/2 - messageLabel.getWidth()/2 , getHeight() - BOTTOM_MESSAGE_MARGIN - messageLabel.getDescent());
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		if (profile != null) {
			assignValuesToVariables(profile);
			addVariablesOnCanvas(profile);
		}
	}
	
	private void assignValuesToVariables(FacePamphletProfile profile) { // sets the values for every variable
		nameLabel.setLabel(profile.getName());
		if (profile.getImage() != null) {
			image = profile.getImage();
			image.setSize(IMAGE_WIDTH , IMAGE_HEIGHT);
		} else {
			image = null;
			emptyImage = new GRect(LEFT_MARGIN , TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN , IMAGE_WIDTH , IMAGE_HEIGHT);
			imageLabel.setLabel("No Image");
		}
		if (profile.getStatus() != null) {
			statusLabel.setLabel(profile.getStatus());
		}
		friendsLabel.setLabel("Friends");
	}
	
	private void addVariablesOnCanvas(FacePamphletProfile profile) { // adds everything on a canvas
		add(nameLabel , LEFT_MARGIN , TOP_MARGIN + nameLabel.getAscent());
		if (image != null) { // checking if a profile has an image
			add(image , LEFT_MARGIN , TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN);
		} else {
			add(emptyImage);
			add(imageLabel , emptyImage.getX() + IMAGE_WIDTH/2 - imageLabel.getWidth()/2 , emptyImage.getY() + IMAGE_HEIGHT/2 );
		}
		if (profile.getStatus() != null) { // checking if a profile has a status
			add(statusLabel , LEFT_MARGIN , TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN);
		}
		add(friendsLabel , getWidth()/2 , TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN);
		if (profile.getFriends() != null) { // checking if a profile has friends
			Iterator <String> friendsIterator = profile.getFriends();
			int i = 1;
			while(friendsIterator.hasNext()) {
				String friendName = friendsIterator.next();
				GLabel friend = new GLabel(friendName);
				friend.setFont(PROFILE_FRIEND_FONT);
				add(friend , friendsLabel.getX() , friendsLabel.getY() + friend.getHeight() * i);
				i++;
			}
		}
	}

	
}

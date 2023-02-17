/*
 * File: FacePamphletDatabaseExtension.java
 * -------------------------------
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import acm.graphics.GImage;

public class FacePamphletDatabaseExtension implements FacePamphletConstants {
	
	private Map <String , FacePamphletProfileExtension> listOfProfiles = new HashMap<String , FacePamphletProfileExtension>();
	private String fileName = "database.txt";
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the database.
	 */
	public FacePamphletDatabaseExtension() {
		readInfoFromDataFile();
	}
	
	private void readInfoFromDataFile() { // reads the information about the profiles from "database.txt" file
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			while(true) {
				String name = br.readLine(); // name
				if(name == null || name.equals("")) {
					break;
				}
				
				FacePamphletProfileExtension profile = new FacePamphletProfileExtension(name); // creating corresponding profile
				
				String status = br.readLine(); // status
				if (!status.equals("null")) {
					profile.setStatus(status);
				}
				
				String imageName = br.readLine(); // imageName
				if (!imageName.equals("null")) {
					try {
						GImage image = new GImage(imageName); // image
						profile.setImage(image);
						profile.setImageName(imageName);
					} catch(Exception e) {}
				}
				
				String friends = br.readLine(); // friends
				if (!friends.equals("")) {
					StringTokenizer st = new StringTokenizer(friends , "]");
					while(st.hasMoreTokens()) {
						String friend = st.nextToken().substring(1);
						profile.addFriend(friend);
					}
				}
				listOfProfiles.put(name , profile); // saving the profile
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error while reading from a data file: " + e);
		}
	}
	
	
	/** 
	 * This method adds the given profile to the database.  If the 
	 * name associated with the profile is the same as an existing 
	 * name in the database, the existing profile is replaced by 
	 * the new profile passed in.
	 */
	public void addProfile(FacePamphletProfileExtension profile) {
		listOfProfiles.put(profile.getName() , profile);
		writeInfoInDataFile();
	}

	
	/** 
	 * This method returns the profile associated with the given name 
	 * in the database.  If there is no profile in the database with 
	 * the given name, the method returns null.
	 */
	public FacePamphletProfileExtension getProfile(String name) {
		return listOfProfiles.get(name);
	}
	
	
	/** 
	 * This method removes the profile associated with the given name
	 * from the database.  It also updates the list of friends of all
	 * other profiles in the database to make sure that this name is
	 * removed from the list of friends of any other profile.
	 * 
	 * If there is no profile in the database with the given name, then
	 * the database is unchanged after calling this method.
	 */
	public void deleteProfile(String name) {
		listOfProfiles.remove(name);
		writeInfoInDataFile();
	}

	
	/** 
	 * This method returns true if there is a profile in the database 
	 * that has the given name.  It returns false otherwise.
	 */
	public boolean containsProfile(String name) {
		if (listOfProfiles.get(name) != null) return true;
		return false;
	}
	
	public void writeInfoInDataFile() { // updates information about profiles in a data file
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(fileName));
			for (String key : listOfProfiles.keySet()) {
				FacePamphletProfileExtension profile = listOfProfiles.get(key);
				writer.println(profile.getName()); // name
				writer.println(profile.getStatus()); // status
				writer.println(profile.getImageName()); // imageName
				if (profile.getFriends()!= null) {
					Iterator<String> friendsIterator = profile.getFriends();
					while(friendsIterator.hasNext()) {
						writer.print("[" + friendsIterator.next() + "]"); // friends
					}
				}
				writer.println();
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error while writing information in a data file");
		}
	}

}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	
	// instance variables
	
	private ArrayList<NameSurferEntry> data = new ArrayList<NameSurferEntry>();
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while(true) {
				String line = reader.readLine();
				
				if (line == null) {
					break;
				} 
				NameSurferEntry object = new NameSurferEntry(line);
				data.add(object);
			}
			reader.close();
		} catch(Exception e) {
			System.out.println("Error while reading a file: " + e);
		}
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		for(int i = 0; i < data.size(); i++) {
			if (data.get(i).getName().equals(name)) {
				NameSurferEntry object = data.get(i);
				return object;
			}
		}
		return null;
	}
}


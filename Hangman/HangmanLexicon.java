/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.FileReader;

import acm.util.*;
import acmx.export.java.util.ArrayList;

public class HangmanLexicon {
	private ArrayList ar = new ArrayList();
/** constructor **/	
	public HangmanLexicon() { 
        try {
            BufferedReader reader = new BufferedReader(new FileReader("HangmanLexicon.txt"));

            while (true) {
                String line = reader.readLine();

                if (line == null) {
                    break;
                }
                ar.add(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("ERROR " + e);
        }
	} 


/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return ar.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		try {
			return (String)ar.get(index);
		} catch(Exception e) {
			System.out.println("ERROR , ILLEGAL INDEX " + e);
			return null;
		}
	};
}

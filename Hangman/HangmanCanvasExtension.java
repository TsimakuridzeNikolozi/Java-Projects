/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import java.awt.Font;
import acm.graphics.*;

public class HangmanCanvasExtension extends GCanvas {
	
	private String incorrectGuesses;
	private GLabel wordLabel;
	private int numberOfRepeatedIncorrectLetters = 0;
	private int numberOfMultipleSameLetters = 0;
	private GImage image = null;
	private GImage startingInterface = null;
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		startingInterface = new GImage("startingInterface.jpg");
		startingInterface.setSize(BEAM_LENGTH + 10, SCAFFOLD_HEIGHT);
		add(startingInterface , getWidth()/2 - BEAM_LENGTH , TOP_BORDER_OFFSET);
		incorrectGuesses = "";
		numberOfRepeatedIncorrectLetters = 0;
		numberOfMultipleSameLetters = 0;
		image = null;
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		if (wordLabel != null) {
			remove(wordLabel);
		}
		wordLabel = new GLabel(word);
		remove(wordLabel);
		Font font1 = new Font("Serif", Font.BOLD, FONT1_HEIGHT);
		wordLabel.setFont(font1);
		add(wordLabel , getWidth()/2 - BEAM_LENGTH - 10 , LABEL1_Y_COORDINATE);
		checkIfWordHasMultipleSameLetters(word);
	}

	private void checkIfWordHasMultipleSameLetters(String word) { // checks how many multiple same letters the word has
		boolean check = false;
		double temporary = 0;
		int numberOfSameLetters = 0;
		for (int i = 0; i < word.length(); i++) {
			for (int j = 0; j < word.length(); j++) {
				if (word.charAt(i) == word.charAt(j) && i != j && word.charAt(i) != '-') {
					numberOfSameLetters++;
					check = true;
				}
			}
			if (check) {
				temporary += 1.0 / (numberOfSameLetters + 1);
			}
			numberOfSameLetters = 0;
			check = false;
		}
		finalAnswer(temporary);
	}
	
	private void finalAnswer(double temporary) { // assigns numberOfRepeatedIncorrectLetters a value
		if (temporary > numberOfMultipleSameLetters) {
			addOrRemoveBodyParts(incorrectGuesses.length() + numberOfRepeatedIncorrectLetters - numberOfMultipleSameLetters , "REMOVE");
		}
		if (temporary - (int)temporary > 0) {
			numberOfMultipleSameLetters = (int)temporary + 1;
		} else {
			numberOfMultipleSameLetters = (int)temporary;
		}
	}
/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		/* You fill this in */
		if (checkIfIncorrectLetterIsRepeated(letter)) {
			numberOfRepeatedIncorrectLetters++;
		} else {
			incorrectGuesses += letter;
		}
		GLabel incorrectGuessesLabel = new GLabel(incorrectGuesses);
		Font font2 = new Font("Serif", Font.BOLD, FONT2_HEIGHT);
		incorrectGuessesLabel.setFont(font2);
		add(incorrectGuessesLabel , wordLabel.getX() , LABEL1_Y_COORDINATE + FONT1_HEIGHT * 5/4);
		addOrRemoveBodyParts(incorrectGuesses.length() + numberOfRepeatedIncorrectLetters - numberOfMultipleSameLetters , "ADD");
	}
	
	private boolean checkIfIncorrectLetterIsRepeated(char letter) { // checks if the entered incorrect letter is the same as the previous ones
		for (int i = 0; i < incorrectGuesses.length(); i++) {
			if (incorrectGuesses.charAt(i) == letter) {
				return true;
			}
		}
		return false;
	}
	
	private void addOrRemoveBodyParts(int deaths , String command) { // draws body parts depending on the amount of incorrect letters
		try {
			if (command == "ADD" && deaths >= 1) {
				addBodyPart(deaths);
			} else if (command == "REMOVE" && deaths >= 1) {
				removeBodyPart(deaths);
			}
		} catch(Exception e) {
			System.out.println("ERROR WHILE OPENING AN IMAGE: " + e);
		}
	}
	
	private void addBodyPart(int deaths) {// adds body parts
		if (image != null) {
			remove(image);
		}
		image = new GImage (deaths + ".jpg");
		image.setSize(IMAGE_WIDTH , IMAGE_HEIGHT);
		add(image , getWidth()/2 - IMAGE_WIDTH / 2 , HEAD_Y_COORDINATE + 30);
		if (deaths == 8) {
			GImage beforeDeath = new GImage("beforeDeath.jpg");
			beforeDeath.setSize(IMAGE_WIDTH , IMAGE_HEIGHT);
			add(beforeDeath , getWidth()/2 - IMAGE_WIDTH / 2 , HEAD_Y_COORDINATE + 30);
			for (int i = 0; i < 5; i++) {
				beforeDeath.move(0 , 3);
				image.move(0 , 3);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					System.out.println("ERROR: " + e);
				}
			}
			remove(beforeDeath);
		}
	}
	
	private void removeBodyPart(int deaths) { // removes body parts
		if (image != null) {
			remove(image);
		}
		if (deaths == 1) {
			add(startingInterface , getWidth()/2 - BEAM_LENGTH , TOP_BORDER_OFFSET);
		} else {
			image = new GImage(deaths - 1 + ".jpg");
			image.setSize(IMAGE_WIDTH , IMAGE_HEIGHT);
			add(image , getWidth()/2 - IMAGE_WIDTH / 2 , HEAD_Y_COORDINATE + 30);
		}
	}
	
/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 40;

//  my constants
	private static final int TOP_BORDER_OFFSET = 20;
	private static final int HEAD_Y_COORDINATE = TOP_BORDER_OFFSET + ROPE_LENGTH;
	private static final int LABEL1_Y_COORDINATE = TOP_BORDER_OFFSET + SCAFFOLD_HEIGHT + 50;
	private static final int FONT1_HEIGHT = 30 , FONT2_HEIGHT = 20;
	
	private static final int IMAGE_WIDTH = 125;
	private static final int IMAGE_HEIGHT = 250;
}

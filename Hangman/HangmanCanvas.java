/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import java.awt.Font;

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	
	private String incorrectGuesses;
	private GLabel wordLabel;
	private int numberOfRepeatedIncorrectLetters = 0;
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		
		GLine scaffold = new GLine(getWidth()/2 - BEAM_LENGTH , TOP_BORDER_OFFSET , getWidth()/2 - BEAM_LENGTH , TOP_BORDER_OFFSET + SCAFFOLD_HEIGHT);
		GLine beam = new GLine(getWidth()/2 - BEAM_LENGTH , TOP_BORDER_OFFSET , getWidth()/2 , TOP_BORDER_OFFSET);
		GLine rope = new GLine(getWidth()/2 , TOP_BORDER_OFFSET , getWidth()/2 , TOP_BORDER_OFFSET + ROPE_LENGTH);
		
		add(scaffold);
		add(beam);
		add(rope);
		
		incorrectGuesses = "";
		numberOfRepeatedIncorrectLetters = 0;
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
		Font font = new Font("Serif", Font.BOLD, FONT1_HEIGHT);
		wordLabel.setFont(font);
		add(wordLabel , getWidth()/2 - BEAM_LENGTH - 10 , LABEL1_Y_COORDINATE);
		
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
		
		addOrRemoveBodyParts(incorrectGuesses.length() + numberOfRepeatedIncorrectLetters);
	}
	
	private boolean checkIfIncorrectLetterIsRepeated(char letter) { // checks if the entered incorrect letter is the same as the previous ones
		for (int i = 0; i < incorrectGuesses.length(); i++) {
			if (incorrectGuesses.charAt(i) == letter) {
				return true;
			}
		}
		return false;
	}
	
	private void addOrRemoveBodyParts(int deaths) { // draws body parts depending on the amount of incorrect letters
		switch (deaths) {
			case 1: addHead();
					break;
					
			case 2: addBody();
					break;
					
			case 3: addLeftArm();
					break;
					
			case 4: addRightArm();
					break;
					
			case 5: addLeftLeg();
					break;
					
			case 6: addRightLeg();
					break;
					
			case 7: addLeftFoot();
					break;
					
			case 8: addRightFoot();
					break;
		}
	}
	
	private void addHead() { // draws head
		GOval head = new GOval(getWidth()/2 - HEAD_RADIUS , HEAD_Y_COORDINATE , 2 * HEAD_RADIUS , 2 * HEAD_RADIUS);
		add(head);
	}
	
	private void addBody() { // draws body
		GLine body = new GLine(getWidth()/2 , BODY_Y1_COORDINATE , getWidth()/2 , BODY_Y2_COORDINATE); 
		add(body);
	}
	
	private void addLeftArm() { // draws left arm
		GLine leftUpperArm = new GLine(getWidth()/2 , UPPER_ARM_Y_COORDINATE , getWidth()/2 - UPPER_ARM_LENGTH , UPPER_ARM_Y_COORDINATE);
		add(leftUpperArm);
		
		GLine leftLowerArm = new GLine(getWidth()/2 - UPPER_ARM_LENGTH , UPPER_ARM_Y_COORDINATE , getWidth()/2 - UPPER_ARM_LENGTH , LOWER_ARM_Y2_COORDINATE);
		add(leftLowerArm);
	}
	
	private void addRightArm() { // draws right arm
		GLine rightUpperArm = new GLine(getWidth()/2 , UPPER_ARM_Y_COORDINATE , getWidth()/2 + UPPER_ARM_LENGTH , UPPER_ARM_Y_COORDINATE);
		add(rightUpperArm);
		
		GLine rightLowerArm = new GLine(getWidth()/2 + UPPER_ARM_LENGTH , UPPER_ARM_Y_COORDINATE , getWidth()/2 + UPPER_ARM_LENGTH , LOWER_ARM_Y2_COORDINATE);
		add(rightLowerArm);
	}
	
	private void addLeftLeg() { // draws left leg
		GLine leftHip = new GLine(getWidth()/2 , BODY_Y2_COORDINATE , getWidth()/2 - HIP_WIDTH , BODY_Y2_COORDINATE);
		add(leftHip);
		
		GLine leftLeg = new GLine(getWidth()/2 - HIP_WIDTH , BODY_Y2_COORDINATE , getWidth()/2 - HIP_WIDTH , BODY_Y2_COORDINATE + LEG_LENGTH);
		add(leftLeg);
	}
	
	private void addRightLeg() { // draws right leg
		GLine rightHip = new GLine(getWidth()/2 , BODY_Y2_COORDINATE , getWidth()/2 + HIP_WIDTH , BODY_Y2_COORDINATE);
		add(rightHip);
		
		GLine rightLeg = new GLine(getWidth()/2 + HIP_WIDTH , BODY_Y2_COORDINATE , getWidth()/2 + HIP_WIDTH , BODY_Y2_COORDINATE + LEG_LENGTH);
		add(rightLeg);
	}
	
	private void addLeftFoot() { // draws left foot
		GLine leftFoot = new GLine(getWidth()/2 - HIP_WIDTH , BODY_Y2_COORDINATE + LEG_LENGTH , getWidth()/2 - HIP_WIDTH - FOOT_LENGTH , BODY_Y2_COORDINATE + LEG_LENGTH);
		add(leftFoot);
	}
	
	private void addRightFoot() { // draws right foot
		GLine rightFoot = new GLine(getWidth()/2 + HIP_WIDTH , BODY_Y2_COORDINATE + LEG_LENGTH , getWidth()/2 + HIP_WIDTH + FOOT_LENGTH , BODY_Y2_COORDINATE + LEG_LENGTH);
		add(rightFoot);
	}

/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
	private static final int TOP_BORDER_OFFSET = 20;
	private static final int HEAD_Y_COORDINATE = TOP_BORDER_OFFSET + ROPE_LENGTH;
	private static final int BODY_Y1_COORDINATE = HEAD_Y_COORDINATE + 2 * HEAD_RADIUS;
	private static final int BODY_Y2_COORDINATE = BODY_Y1_COORDINATE + BODY_LENGTH;
	private static final int UPPER_ARM_Y_COORDINATE = BODY_Y1_COORDINATE + ARM_OFFSET_FROM_HEAD;
	private static final int LOWER_ARM_Y2_COORDINATE = UPPER_ARM_Y_COORDINATE + LOWER_ARM_LENGTH;
	private static final int LABEL1_Y_COORDINATE = TOP_BORDER_OFFSET + SCAFFOLD_HEIGHT + 50;
	private static final int FONT1_HEIGHT = 30 , FONT2_HEIGHT = 20;
}

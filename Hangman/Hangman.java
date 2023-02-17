/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
	
	// instance variables
	private HangmanCanvas canvas;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private String currentWord, currentGuessWord = "";
	private int guessesLeft;
	private boolean checkIfLetterIsAlreadyWritten = false;
	
	public void init() { // canvas
		canvas = new HangmanCanvas(); 
		add(canvas); 
	} 
	
    public void run() { // start
    	canvas.reset();
    	
    	HangmanLexicon object = new HangmanLexicon();
    	println("WELCOME TO HANGMAN!");
    	chooseRandomWord(object);
    	guessesLeft = 8;
    	
    	while(guessesLeft != 0 && !currentGuessWord.equals(currentWord)) {
    		displayMessages();
    	}
	}
    
    private void chooseRandomWord(HangmanLexicon object) { // chooses a random word
    	int index = rgen.nextInt(0 , object.getWordCount() - 1);
    	currentWord = object.getWord(index);
    	currentGuessWord = "";
    	
    	for (int i = 0; i < currentWord.length(); i++) {
    		currentGuessWord += "-";
    	}
    	
    	canvas.displayWord(currentGuessWord);
    }
    
    private void displayMessages() { // displays messages in the console
    	println("THE WORD NOW LOOKS LIKE THIS: " + currentGuessWord);
    	println("YOU HAVE " + guessesLeft + " GUESSES LEFT.");
    	
    	char inputChar;
    	String inputLine;
    	
    	inputLine = readLine("YOUR GUESS: ");
    	
    	while (inputLine.equals("") || ((int)inputLine.charAt(0) < 65 || (int)inputLine.charAt(0) > 122)) {
    		println("WRONG INPUT, PLEASE ENTER A LETTER");
    		inputLine = readLine("YOUR GUESS: ");
    	}
    	
    	inputChar = inputLine.charAt(0);
    	inputChar = convertLowerToUpper(inputChar);
    	
    	if (checkLetter(inputChar) && !checkIfLetterIsAlreadyWritten) {
    		println("YOUR GUESS IS RIGHT!");
    		checkIfWon();
    		
    	} else if (checkIfLetterIsAlreadyWritten){
    		println("YOU HAVE ALREAY GUESSED THAT LETTER");
    		
    	} else {
    		println("THERE ARE NO " + inputChar + "'S IN THIS WORD");
    		checkIfLost();
    	}
    	
    	println("\n");
    }
    
    private char convertLowerToUpper(char inputChar) { // turns the lowercase character into an uppercase one.
    	if (inputChar - 'a' >= 0) {
			return (char)('A' + (inputChar - 'a'));
    	}
    	return inputChar;
    }
    
    private boolean checkLetter(char inputChar) { // checks if the letter entered is the part of the word
    	boolean checkIfTrue = false;
    	String temporaryWord = currentGuessWord;
    	currentGuessWord = "";
    	checkIfLetterIsAlreadyWritten = false;
    	
    	for (int i = 0; i < currentWord.length(); i++) {
    		if (temporaryWord.charAt(i) != '-') {
    			if (temporaryWord.charAt(i) == inputChar) {
    				checkIfLetterIsAlreadyWritten = true;
    			}
    			currentGuessWord += temporaryWord.charAt(i);
    			
    		} else if (currentWord.charAt(i) == inputChar) {
				currentGuessWord += inputChar;
    			checkIfTrue = true;
    			
    		} else {
    			currentGuessWord += "-";
    		}
    	}
    	canvas.displayWord(currentGuessWord);
    	
    	if (!checkIfTrue && !checkIfLetterIsAlreadyWritten) {
    		guessesLeft--;
    		canvas.noteIncorrectGuess(inputChar);
    		return false;
    	}
    	return true;
    }
    
    private void checkIfWon() { // what to do when player wins
    	if (currentGuessWord.equals(currentWord)) {
    		println("YOU WON!!!");
    		println("THE WORD WAS: --> " + currentWord);
    		wonOrLost("WON");
    	}
    }
    
    private void checkIfLost() { // what to do when player loses
    	if (guessesLeft == 0) {
    		println("YOU WERE HANGED :(");
    		println("THE WORD WAS: --> " + currentWord);
    		wonOrLost("LOST");
    	}
    }
    
    private void wonOrLost(String result) { // displays a label depending on the result of the game
    	canvas.removeAll();
    	
    	Font font = new Font("Serif", Font.BOLD, 30);
    	Font font1 = new Font("Serif", Font.BOLD, 20);
    	
		GLabel wonOrLost = new GLabel("YOU " + result);
		wonOrLost.setFont(font);
		canvas.add(wonOrLost , canvas.getWidth()/2 - wonOrLost.getWidth()/2 , canvas.getHeight()/2 - wonOrLost.getHeight()/2);
		
		GLabel theWord = new GLabel("THE WORD WAS " + currentWord);
		theWord.setFont(font1);
		canvas.add(theWord , canvas.getWidth()/2 - theWord.getWidth()/2 , canvas.getHeight()/2 - wonOrLost.getHeight()/2 + theWord.getHeight());
	}

}

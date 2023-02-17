/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.AudioClip;
import java.awt.*;

public class HangmanExtension extends ConsoleProgram {
	
	// instance variables
	private HangmanCanvasExtension canvas;
	private HangmanLexicon object = new HangmanLexicon();
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private String currentWord, currentGuessWord = "";
	private int guessesLeft;
	private boolean checkIfLetterIsAlreadyWritten = false;
	private int numberOfGames = 1 , numberOfWins = 0 , numberOfLoses = 0;
	
	public void init() { // canvas
		canvas = new HangmanCanvasExtension(); 
		add(canvas); 
	} 
	
    public void run() { // start
    	canvas.reset();
    	
    	if (numberOfGames == 1) {
    		println("WELCOME TO HANGMAN!");
    	}
    	
    	println("GAME -> " + numberOfGames + "\n");
    	numberOfGames++;
    	
    	chooseRandomWord(object);
    	guessesLeft = 8;
    	
    	GLabel wins = new GLabel("WINS: " + numberOfWins);
    	GLabel loses = new GLabel("LOSES: " + numberOfLoses);
    	
    	Font font = new Font("Serif", Font.BOLD, 25);
    	wins.setFont(font);
    	loses.setFont(font);
    	
    	canvas.add(wins , canvas.getWidth() - loses.getWidth() - 5 , wins.getHeight());
    	canvas.add(loses , canvas.getWidth() - loses.getWidth() - 5 , wins.getHeight() + loses.getHeight());
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
    	
    	String inputLine;
    	char inputChar;
    	
    	inputLine = readLine("YOUR GUESS: ");
    	
    	while (inputLine.equals("") || ((int)inputLine.charAt(0) < 65 || (int)inputLine.charAt(0) > 122)) {
    		println("WRONG INPUT, PLEASE ENTER A LETTER");
    		inputLine = readLine("YOUR GUESS: ");
    	}
    	
    	inputChar = inputLine.charAt(0);
    	inputChar = convertLowerToUpper(inputChar);
    	
    	if (checkLetter(inputChar) && !checkIfLetterIsAlreadyWritten) {
    		println("YOUR GUESS IS RIGHT!");
    		
    		AudioClip correctAnswer = MediaTools.loadAudioClip("correct.au");
    		if (!checkIfWon()) {
    			correctAnswer.play();
    		}
    		
    	} else if (checkIfLetterIsAlreadyWritten){
    		println("YOU HAVE ALREAY GUESSED THAT LETTER");
    		
    	} else {
    		println("THERE ARE NO " + inputChar + "'S IN THIS WORD");
    		
    		AudioClip wrongAnswer = MediaTools.loadAudioClip("wrong.au");
    		if (!checkIfLost()) {
    			wrongAnswer.play();
    		}
    	}
    	println("\n");
    }
    
    private char convertLowerToUpper(char inputChar) { // turns the lowercase character into an uppercase one.
    	if (inputChar - 'a' >= 0) {
			return (char)('A' + (inputChar - 'a'));
    	}
    	return inputChar;
    }
    
    private boolean checkLetter(char inputChar) { // checks if the character entered is the part of the word
    	boolean checkIfTrue = false;
    	String temporaryWord = currentGuessWord;
    	int numberOfCorrectLetters = 0;
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
    			numberOfCorrectLetters++;
    			
    		} else {
    			currentGuessWord += "-";
    		}
    	}
    	canvas.displayWord(currentGuessWord);
    	
    	if (!checkIfTrue && !checkIfLetterIsAlreadyWritten) {
    		guessesLeft--;
    		canvas.noteIncorrectGuess(inputChar);
    		return false;
    		
    	} else if(numberOfCorrectLetters > 1 && !checkIfLetterIsAlreadyWritten) {
    		guessesLeft++;
    	}
    	
    	return true;
    }
    
    private boolean checkIfWon() { // what to do when player wins
    	if (currentGuessWord.equals(currentWord)) {
    		AudioClip win = MediaTools.loadAudioClip("win.au");
    		win.play();
    		pause(1000);
    		
    		println("YOU WON!!!");
    		println("THE WORD WAS: --> " + currentWord);
    		
    		wonOrLost("WON");
    		numberOfWins++;
    		startOver();
    		return true;
    	}
    	return false;
    }
    
    private boolean checkIfLost() { // what to do when player loses
    	if (guessesLeft == 0) {
    		AudioClip lose = MediaTools.loadAudioClip("lose.au");
    		lose.play();
    		pause(4000);
    		
    		println("YOU WERE HANGED :(");
    		println("THE WORD WAS: --> " + currentWord);
    		
    		wonOrLost("LOST");
    		numberOfLoses++;
    		startOver();
    		return true;
    	}
    	return false;
    }
    
    private void wonOrLost(String result) { // displays a label depending on the result of the game
    	canvas.removeAll();
    	
    	Font font = new Font("Serif", Font.BOLD, 30);
    	Font font1 = new Font("Serif", Font.BOLD, 20);
    	
    	if (result.equals("WON")) {
    		GImage victoryImage = new GImage("victory.jpg");
        	victoryImage.setSize(125 , 250);
        	canvas.add(victoryImage , canvas.getWidth()/2 - victoryImage.getWidth()/2 ,  canvas.getHeight()/2 - victoryImage.getHeight()/2);
        	
    		GLabel gratitude = new GLabel("THANK YOU!!!");
    		gratitude.setFont(font);
    		canvas.add(gratitude , canvas.getWidth()/2 - gratitude.getWidth()/2 , 80);
    		
    	} else {
    		GImage lossImage = new GImage("loss.jpg");
    		lossImage.setSize(250 , 250);
        	canvas.add(lossImage , canvas.getWidth()/2 - lossImage.getWidth()/2 ,  canvas.getHeight()/2 - lossImage.getHeight()/2);
    	}
    	
		GLabel wonOrLost = new GLabel("YOU " + result + "!!!");
		wonOrLost.setFont(font);
		canvas.add(wonOrLost , canvas.getWidth()/2 - wonOrLost.getWidth()/2 , 40);
		
		GLabel theWord = new GLabel("THE WORD WAS " + currentWord);
		theWord.setFont(font1);
		canvas.add(theWord , canvas.getWidth()/2 - theWord.getWidth()/2 , canvas.getHeight() - 40);
		
		pause(5000);
		canvas.removeAll();
	}
	
	private void startOver() { // restarts the game
		int i = 3;
		GLabel startOver;
		Font font = new Font("Serif", Font.BOLD, 30);
		while(i > 0) {
			startOver = new GLabel("STARTING OVER IN " + i);
			startOver.setFont(font);
			canvas.add(startOver , canvas.getWidth()/2 - startOver.getWidth()/2 , canvas.getHeight()/2 - startOver.getHeight()/2);
			pause(1000);
			canvas.remove(startOver);
			i--;
		}
		println("\n" + "------------------------------------------------" + "\n");
		run();
	}

}

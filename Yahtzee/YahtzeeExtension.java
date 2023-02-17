import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.io.*;
import acm.program.*;
import acm.util.*;

public class YahtzeeExtension extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new YahtzeeExtension().start(args);
	}
	
	public void run() {
		if (gamesPlayed == 0) addMouseListeners();
		IODialog dialog = getDialog();
		getPlayers(dialog);
		showTop10();
		addButton("START");
		buttonPressed("START");
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}
	
	private void getPlayers(IODialog dialog) { // getting the information about the players
		nPlayers = dialog.readInt("Enter number of players");
		while(nPlayers > 4) {
			nPlayers = dialog.readInt("Number of players has to be less than five");
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
			checkIfPLayerNameIsEmpty(i , dialog);
			checkIfPLayerNameIsUsed(i , dialog);
			checkIfPlayerNameIsTooLong(i , dialog);
		}
	}
	
	private void checkIfPLayerNameIsEmpty(int i , IODialog dialog) { // checks if the name is empty or starts with 'space'
		while (playerNames[i-1].equals("") || playerNames[i-1].charAt(0) == ' ') {
			playerNames[i - 1] = dialog.readLine("The name can't be empty and can't start with 'space', enter a different one for player " + i);
			checkIfPLayerNameIsUsed(i , dialog);
			checkIfPlayerNameIsTooLong(i , dialog);
		}
	}
	
	private void checkIfPLayerNameIsUsed(int i , IODialog dialog) { // checks if the name has already been used in the current session
		for (int j = 0; j < i - 1; j++) {
			while (playerNames[i - 1].equals(playerNames[j])) {
				playerNames[i - 1] = dialog.readLine("Can't Use That Name, Enter a different one for player " + i);
				checkIfPlayerNameIsTooLong(i , dialog);
				checkIfPLayerNameIsEmpty(i , dialog);
			}
		}
	}
	
	private void checkIfPlayerNameIsTooLong(int i , IODialog dialog) { // checks if the name can fit the table boxes
		while(playerNames[i - 1].length() > 9) {
			playerNames[i - 1] = dialog.readLine("The name has to be less than 10 characters, Enter a shorter one for player " + i);
			checkIfPLayerNameIsUsed(i , dialog);
			checkIfPLayerNameIsEmpty(i , dialog);
		}
	}

	private void playGame() { // the process of a game
		assignBaseValues();
				
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			for (int playerIndex = 0; playerIndex < playerNames.length; playerIndex++) {
				firstTurn(playerIndex);
				secondAndThirdTurns();
				
				int category = display.waitForPlayerToSelectCategory(); // category which a player chooses
				
				while (scoresInEachCategory[playerIndex][category-1] != -1 && category != YAHTZEE) { // rejecting a player if he chooses the same category multiple times
					display.printMessage("You already used that category, choose another one");
					category = display.waitForPlayerToSelectCategory();
				}
				
				updateInformation(playerIndex , category);
				updateUpperScore(playerIndex , false);
				updateLowerScore(playerIndex , false);
				
				display.updateScorecard(TOTAL, playerIndex+1 , scoresInEachCategory[playerIndex][TOTAL - 1]);	// updates total score
			}
		}
		gamesPlayed++;
		for (int playerIndex = 0; playerIndex < playerNames.length; playerIndex++) {
			updateUpperScore(playerIndex , true);
			updateLowerScore(playerIndex , true);
		}
		actionsAftertheGameIsComplete();
	}
	
	private void showTop10() { // adds labels and calls addTop10 method
		getTop10();
		GLabel top = new GLabel("Current Top 10 Scores");
		top.setFont(new Font("Arial", Font.BOLD, 20));
		double yPlacement = 2 * top.getHeight();
		add(top, getWidth()/2 - top.getWidth()/2, top.getHeight());
		
		GLabel names = new GLabel("NAMES");
		names.setFont(new Font("Arial" , Font.BOLD , 15));
		add(names , getWidth()/2 - 50 - names.getWidth()/2 , yPlacement);
		
		GLabel scores = new GLabel("SCORES");
		scores.setFont(new Font("Arial" , Font.BOLD , 15));
		add(scores , getWidth()/2 + 50 - scores.getWidth()/2 , yPlacement);
		
		yPlacement += top.getHeight();
		
		addTop10(yPlacement);
	}
	
	private void addTop10(double yPlacement) { // draws the table and adds the corresponding names and scores as labels
		for(int i = 0; i < top10Scores.length; i++) {
			
			GLabel player = new GLabel(top10Names.get(i));
			player.setFont(new Font("Arial" , Font.PLAIN , 15));
			
			GLabel score = new GLabel(Integer.toString(top10Scores[i]));
			score.setFont(new Font("Arial" , Font.PLAIN , 15));
			
			GRect nameRect = new GRect(getWidth()/2 - 100 , yPlacement - 15 , 100 , 20);
			GRect scoreRect = new GRect(getWidth()/2 , yPlacement - 15 , 100 , 20);
			if (i <= 2) {nameRect.setFilled(true); scoreRect.setFilled(true);}
			switch (i) {
				case 0: nameRect.setFillColor(Color.yellow); scoreRect.setFillColor(Color.yellow); break;
				case 1: nameRect.setFillColor(Color.LIGHT_GRAY); scoreRect.setFillColor(Color.LIGHT_GRAY); break;
				case 2: nameRect.setFillColor(new Color(180, 159, 7)); scoreRect.setFillColor(new Color(180, 159, 7)); break; // colour bronze
			}
			if (newTop10PlayerIndexes.size() != 0) {
				for (int j = 0; j < newTop10PlayerIndexes.size(); j++) {
					if (i == newTop10PlayerIndexes.get(j)) {
						nameRect.setFilled(true);
						nameRect.setFillColor(Color.red);
						scoreRect.setFilled(true);
						scoreRect.setFillColor(Color.red);
					}
				}
			}
			add(nameRect); add(scoreRect);
			add(player , getWidth()/2 - player.getWidth()/2 - 50 , yPlacement); add(score , getWidth()/2 - score.getWidth()/2 + 50 , yPlacement);
			yPlacement += nameRect.getHeight();
		}
	}
	
	private void addButton(String command) { // adds a "START" or "START OVER" GRect that acts as a button
		button = new GRect(getWidth()/2 - 40 , getHeight() - 40 , 80 , 20);
		button.setFilled(true);
		button.setColor(Color.lightGray);
		add(button);
		
		startLabel = new GLabel(command);
		startLabel.setFont(new Font("Arial" , Font.BOLD , 12));
		add(startLabel , button.getX() + button.getWidth()/2 - startLabel.getWidth()/2 , button.getY() + 1.25 * startLabel.getAscent());
	}
	
	public void mouseClicked(MouseEvent e) { // doing corresponding actions if a player clicks on a button
		if ( (getElementAt(e.getX(), e.getY()) == button || getElementAt(e.getX(), e.getY()) == startLabel) && startLabel.getLabel().equals("START") ) {
			startButtonIsPressed = true;
		} else if ((getElementAt(e.getX(), e.getY()) == button || getElementAt(e.getX(), e.getY()) == startLabel) && startLabel.getLabel().equals("START OVER")) {
			startOverButtonIsPressed = true;
		}
	}
	
	private void buttonPressed(String command) { // waits until the button is pressed and than does corresponding actions depending on a button command
		if (command == "START") {
			while(!startButtonIsPressed) {}
			startButtonIsPressed = false;
			clickAction();
			removeAll();
		} else if (command == "START OVER") {
			while(!startOverButtonIsPressed) {}
			startOverButtonIsPressed = false;
			clickAction();
			newTop10Players.clear();
			newTop10PlayerIndexes.clear();
			removeAll();
			run();
		}
	}
	
	private void clickAction() { // click animation
		button.setColor(Color.GRAY);
		pause(100);
		button.setColor(Color.lightGray);
		pause(100);
	}
	
	private void getTop10() { // gets the top 10 players and their scores from the top10.txt file
		try {
            BufferedReader reader = new BufferedReader(new FileReader("top10.txt"));
            for (int i = 0; i < top10Scores.length; i++) {
                String name = reader.readLine();
                String score = reader.readLine();
                top10Names.add(i, name);
                top10Scores[i] = Integer.parseInt(score);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("ERROR " + e);
        }
	}
	
	private void assignBaseValues() { // base values of arrays and arrayLists
		scoresInEachCategory = new int[playerNames.length][N_CATEGORIES]; // saves the points of each player in each category
		assignBaseValueToArray(); // assigns base values to "scoresInEachCategory" array
		newTop10Players.clear();
		newTop10PlayerIndexes.clear();
		numberOfYahtzeesForEachPlayer = new int[playerNames.length];
	}
	
	private void assignBaseValueToArray() { // assigns -1 as a base value for an array that contains scores of each category
		for (int i = 0; i < scoresInEachCategory.length; i++) {
			for (int j = 0; j < scoresInEachCategory[i].length; j++) {
				if (j == TOTAL - 1 || j == UPPER_BONUS - 1) {
					scoresInEachCategory[i][j] = 0;
				} else {
					scoresInEachCategory[i][j] = -1;
				}
			}
		}
	}
	
	private void firstTurn(int playerIndex) { // first turn at rolling
		display.printMessage(playerNames[playerIndex] + "'s turn."); 
		display.waitForPlayerToClickRoll(playerIndex+1);
		
		generateDice("FIRST");
		AudioClip roll = MediaTools.loadAudioClip("roll.au");
		roll.play();
		display.displayDice(dice);
	}
	
	private void secondAndThirdTurns() { // what to do on a second and third dice rolls
		for (int i = 0; i < 2; i++) {
			display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
			display.waitForPlayerToSelectDice();
			
			generateDice("SECOND OR THIRD");
			AudioClip roll = MediaTools.loadAudioClip("roll.au");
			roll.play();
			display.displayDice(dice);
		}
	}
	
	private void generateDice(String turn) { // generates a random array of integers which is then used to generate dices
		int[] indexesOfSelectedDice = getSelectedDiceindexes();
		for (int i = 0; i < N_DICE; i++) {
			if (turn == "FIRST") {
				dice[i] = rgen.nextInt(1 , 6);
			} else if (indexesOfSelectedDice[i] != -1) {
				dice[i] = rgen.nextInt(1 , 6);
			}
		}
	}
	
	private int[] getSelectedDiceindexes() { // gets the indexes of selected dices and saves them in an array , the ones which aren't selected are assigned a value -1
		int []indexesOfSelectedDice = new int[N_DICE];
		for (int i = 0; i < N_DICE; i++) {
			if (display.isDieSelected(i)) {
				indexesOfSelectedDice[i] = i;
			} else {
				indexesOfSelectedDice[i] = -1;
			}
		}
		return indexesOfSelectedDice;
	}
	
	private void updateInformation(int playerIndex , int category) { // updates arrays and score card , calls generatePoints method
		int newPoints = generatePoints(category , playerIndex);
		if (category == YAHTZEE) {
			if(numberOfYahtzeesForEachPlayer[playerIndex] == 1) {
				scoresInEachCategory[playerIndex][category-1] = newPoints;
			} else if (numberOfYahtzeesForEachPlayer[playerIndex] == 0){
				scoresInEachCategory[playerIndex][category-1] = 0;
			} else {
				scoresInEachCategory[playerIndex][category-1] += newPoints;
			}
		} else {
			scoresInEachCategory[playerIndex][category-1] = newPoints;
		}
		scoresInEachCategory[playerIndex][TOTAL - 1] += newPoints;
		
		display.updateScorecard(category, playerIndex+1 , scoresInEachCategory[playerIndex][category-1]);
	}
	
	private void updateUpperScore(int playerIndex , boolean theGameIsOver) { // updates upper score
		if ( (upperOrLowerIsComplete(scoresInEachCategory[playerIndex] , "UPPER") && scoresInEachCategory[playerIndex][UPPER_SCORE - 1] == -1) || theGameIsOver) {
			int upperScore =  completeUpperOrLowerScore(scoresInEachCategory[playerIndex] , "UPPER");
			display.updateScorecard(UPPER_SCORE, playerIndex+1 , upperScore);
			scoresInEachCategory[playerIndex][UPPER_SCORE - 1] = upperScore;
			if (upperScore >= MINIMAL_SCORE_FOR_UPPER_BONUS) {
				display.updateScorecard(UPPER_BONUS, playerIndex+1 , UPPER_BONUS_POINTS);
				scoresInEachCategory[playerIndex][UPPER_BONUS - 1] = UPPER_BONUS_POINTS;
				scoresInEachCategory[playerIndex][TOTAL - 1] += UPPER_BONUS_POINTS;
			}
		}
	}
	
	private void updateLowerScore(int playerIndex , boolean theGameIsOver) { // updates lower score
		if ( (upperOrLowerIsComplete(scoresInEachCategory[playerIndex] , "LOWER") && scoresInEachCategory[playerIndex][LOWER_SCORE - 1] == -1) || theGameIsOver) {
			int lowerScore =  completeUpperOrLowerScore(scoresInEachCategory[playerIndex] , "LOWER");
			scoresInEachCategory[playerIndex][LOWER_SCORE - 1] = lowerScore;
			display.updateScorecard(LOWER_SCORE, playerIndex+1 , lowerScore);
		}
	}
	
	private boolean upperOrLowerIsComplete(int[] categories , String command) { // checks if upper or lower parts are complete
		if (command.equals("UPPER")){
			for (int i = 0; i < UPPER_SCORE - 1; i++) {
				if (categories[i] == -1) {
					return false;
				}
			}
		} else if (command.equals("LOWER")) {
			for(int i = THREE_OF_A_KIND - 1; i < LOWER_SCORE - 1; i++) {
				if (categories[i] == -1) {
					return false;
				}
			}
		}
		return true;
	}
	
	private int completeUpperOrLowerScore(int[] totalPoints , String command) { // returns the sum of complete upper or lower score
		int sum = 0;
		if (command.equals("UPPER")) {
			for (int i = 0; i < UPPER_SCORE - 1; i++) {
				sum += totalPoints[i];
			}
		} else if (command.equals("LOWER")) {
			for(int i = THREE_OF_A_KIND - 1; i < LOWER_SCORE - 1; i++) {
				sum += totalPoints[i];
			}
		}
		return sum;
	}
	
	private void actionsAftertheGameIsComplete() { // what do do when the game is complete
		int winner =  getWinner();
		display.printMessage("Congratulations " + playerNames[winner] + "! You are the winner with the total score of " + scoresInEachCategory[winner][TOTAL-1]);
		pause(4000);
		if (anyoneHasTop10Score()) {
			updateTop10Scores();
		} else {
			pause(4000);
			removeAll();
			addButton("START OVER");
			buttonPressed("START OVER");
		}
	}
	
	private int getWinner() { // returns the index of a winner
		int max = -1 , index = -1;
		for(int i = 0; i < scoresInEachCategory.length; i++) {
			if (scoresInEachCategory[i][TOTAL - 1] > max) {
				max = scoresInEachCategory[i][TOTAL - 1];
				index = i;
			}
		}
		return index;
	}
	
	private boolean anyoneHasTop10Score() { // checks if any of the players deserve being in top 10
		for (int i = 0; i < scoresInEachCategory.length; i++) {
			if (scoresInEachCategory[i][TOTAL - 1] > top10Scores[top10Scores.length - 1]) {
				return true;
			}
		}
		return false;
	}
	
	private void updateTop10Scores() { // updates an array of top 10 scores. calls getNewTop10ScoreOwners method
		int[] everyScore = new int[10 + playerNames.length];
		int[] temporary = top10Scores;
		for(int i = 0; i < top10Scores.length; i++) {
			everyScore[i] = top10Scores[i];
		}
		for(int i = top10Scores.length; i < top10Scores.length + playerNames.length; i++) {
			everyScore[i] = scoresInEachCategory[i-10][TOTAL-1];
		}
		Arrays.sort(everyScore);
		for(int i = 0; i < top10Scores.length; i++) {
			top10Scores[i] = everyScore[everyScore.length - 1 - i];
		}
		getNewTop10ScoreOwners(temporary);
	}
	
	private void getNewTop10ScoreOwners(int[] temporary) { //  updates the top10Names ArrayList and saves the indexes of new top 10 players
		newTop10Players = new ArrayList<String>();
		for (int i = 0; i < playerNames.length; i++) {
			for(int j = 0; j < top10Scores.length; j++) {
				if (scoresInEachCategory[i][TOTAL-1] == top10Scores[j]) {
					newTop10Players.add(playerNames[i]);
					top10Names.add(j , playerNames[i]);
					break;
				}
			}
		}
		for (int i = 0; i < playerNames.length; i++) { // saving indexes
			for(int j = 0; j < top10Scores.length; j++) {
				if ((top10Scores[j] == scoresInEachCategory[i][TOTAL-1]) && (top10Names.get(j).equals(playerNames[i])) ) {
					newTop10PlayerIndexes.add(j);
				}
			}
		}
		notifyNewTop10ScoreOwners(newTop10Players);
		countdown();
	}
	
	private void notifyNewTop10ScoreOwners(ArrayList<String> newTop10Players) { // congratulates the new top10 score owners
		removeAll();
		String names = "";
		for(int i = 0; i < newTop10Players.size(); i++) {
			if (i != newTop10Players.size() - 1) {
				names += newTop10Players.get(i) + " , ";
			} else {
				names += newTop10Players.get(i) + " !!!";
			}
		}
		GLabel congratulations = new GLabel("Congratulations");
		GLabel topNames = new GLabel(names);
		GLabel top = new GLabel("You Entered The Top 10");
		Font myFont = new Font("Arial" , Font.BOLD , 20);
		congratulations.setFont(myFont);
		topNames.setFont(myFont);
		top.setFont(myFont);
		add(congratulations , getWidth()/2 - congratulations.getWidth()/2 , 30);
		add(topNames , getWidth()/2 - topNames.getWidth()/2 , 60);
		add(top , getWidth()/2 - top.getWidth()/2 , 90);
	}
	
	private void countdown() { //  pauses for 5 seconds , updates the top10.txt file and than displays the renewed Top 10
		pause(5000);
		removeAll();
		try {
			PrintWriter writer = new PrintWriter(new FileWriter("top10.txt"));
			writer.print("");
			for(int i = 0; i < top10Scores.length; i++) {
				writer.println(top10Names.get(i));
				writer.println(top10Scores[i]);
			}
			writer.close();
		} catch (Exception e){
			System.out.println("ERROR " + e);
		}
		showTop10();
		addButton("START OVER");
		buttonPressed("START OVER");
	}
	
	private int generatePoints(int category , int playerIndex) { // returns points depending on category
		switch(category) {
			case ONES:
			case TWOS:
			case THREES: 
			case FOURS: 
			case FIVES: 
			case SIXES: return checkCategory1To6(category);
			case THREE_OF_A_KIND: 
			case FOUR_OF_A_KIND:
			case FULL_HOUSE:
			case YAHTZEE: return checkCategory9_10_11_14(category , playerIndex);
			case SMALL_STRAIGHT:
			case LARGE_STRAIGHT: return checkCategory12And13(category);
			case CHANCE: return checkCategory15();
			default: return 0;
		}
	}
	
	private int checkCategory1To6(int category) { // generates points for categories 1-6
		int points = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == category) {
				points += dice[i];
			}
		}
		return points;
	}
	
	private int checkCategory9_10_11_14(int category , int playerIndex) { // generates points for categories "three of a kind" , "four of a kind" , "full house" and "Yahtzee"
		for (int i = 0; i < N_DICE; i++) {
			int points = 0;
			int numberOfSameDice = 0;
			for(int j = 0; j < N_DICE; j++) {
				if (dice[i] == dice[j]) {
					numberOfSameDice ++;
				} else {
					if (category == YAHTZEE) return 0;
				}
				points += dice[j];
			}
			if (numberOfSameDice == 3 && category == FULL_HOUSE) { // checking for "full house"
				for(int a = 0; a < N_DICE; a++) {
					for(int b = 0; b < N_DICE; b++) {
						if (dice[a] == dice[b] && dice[a] != dice[i]) {
							return 25;
						}
					}
				}
			} 
			else if( (numberOfSameDice >= 3 && category == THREE_OF_A_KIND) || (numberOfSameDice >= 4 && category == FOUR_OF_A_KIND) ) return points;
			else if (numberOfSameDice == 5 && category == YAHTZEE) {
				if (numberOfYahtzeesForEachPlayer[playerIndex] == 0) {
					numberOfYahtzeesForEachPlayer[playerIndex]++;
					return 50;
				} else if(numberOfYahtzeesForEachPlayer[playerIndex] > 0) {
					numberOfYahtzeesForEachPlayer[playerIndex]++;
					return 100;
				}
			}
		}
		return 0;
	}
	
	private int checkCategory12And13(int category) { // generates points for "small straight" and "large straight"
		int streak = 0;
		Arrays.sort(dice);
		for (int i = 0; i < N_DICE - 1; i++) {
			if (dice[i+1] - dice[i] == 1) {
				streak++;
			} else if (dice[i+1] == dice[i]){
				continue;
			} else {
				streak = 0;
			}
			if(streak >= 3 && category == SMALL_STRAIGHT) {
				return 30;
			} else if (streak == 4 && category == LARGE_STRAIGHT) {
				return 40;
			}
		}
		return 0;
	}
	
	private int checkCategory15() { // generates points for "chance"
		int points = 0;
		for (int i = 0; i < N_DICE; i++) {
			points += dice[i];
		}
		return points;
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

/* My instance variables */
	private int[][] scoresInEachCategory;
	private int[] dice = new int[N_DICE] , top10Scores = new int[10];
	private ArrayList<String> top10Names = new ArrayList<String>();
	private GRect button;
	private GLabel startLabel;
	private volatile boolean startButtonIsPressed = false , startOverButtonIsPressed = false;
	private ArrayList <String> newTop10Players = new ArrayList<String>();
	private ArrayList<Integer> newTop10PlayerIndexes = new ArrayList<Integer>();
	private int[] numberOfYahtzeesForEachPlayer;
	private int gamesPlayed = 0;
}


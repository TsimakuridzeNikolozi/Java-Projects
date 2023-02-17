/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzeee extends GraphicsProgram implements YahtzeeConstants {
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		while(nPlayers > 4) {
			nPlayers = dialog.readInt("Number of players has to be less than five");
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		scoresInEachCategory = new int[playerNames.length][N_CATEGORIES]; // saves the points of each player in each category
		assignBaseValueToArray(); // assigns base values to "scoresInEachCategory" array
		
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			for (int playerIndex = 0; playerIndex < playerNames.length; playerIndex++) {
				firstTurn(playerIndex);
				secondAndThirdTurns();
				
				int category = display.waitForPlayerToSelectCategory(); // category which a player chooses
				
				while (scoresInEachCategory[playerIndex][category-1] != -1) { // rejecting a player if he chooses the same category multiple times
					display.printMessage("You already used that category, choose another one");
					category = display.waitForPlayerToSelectCategory();
				}
				
				updateInformation(playerIndex , category);
				updateUpperScore(playerIndex);
				updateLowerScore(playerIndex);
				
				display.updateScorecard(TOTAL, playerIndex+1 , scoresInEachCategory[playerIndex][TOTAL - 1]);	// updates total score
			}	
		}
		int winner =  getWinner();
		display.printMessage("Congratulations " + playerNames[winner] + "! You are the winner with the total score of " + scoresInEachCategory[winner][TOTAL-1]);
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
		display.displayDice(dice);
	}
	
	private void secondAndThirdTurns() { // what to do on a second and third dice rolls
		for (int i = 0; i < 2; i++) {
			display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
			display.waitForPlayerToSelectDice();
			
			generateDice("SECOND OR THIRD");
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
	
	private void updateInformation(int playerIndex , int category) { // updates arrays and score card
		int newPoints = generatePoints(category); 
		scoresInEachCategory[playerIndex][category-1] = newPoints;
		scoresInEachCategory[playerIndex][TOTAL - 1] += newPoints;
		
		display.updateScorecard(category, playerIndex+1 , newPoints);
	}
	
	private void updateUpperScore(int playerIndex) { // updates upper score
		if (upperOrLowerIsComplete(scoresInEachCategory[playerIndex] , "UPPER") && scoresInEachCategory[playerIndex][UPPER_SCORE - 1] == -1) {
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
	
	private void updateLowerScore(int playerIndex) { // updates lower score
		if (upperOrLowerIsComplete(scoresInEachCategory[playerIndex] , "LOWER") && scoresInEachCategory[playerIndex][LOWER_SCORE - 1] == -1) {
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
	
	private int generatePoints(int category) { // returns points depending on category
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
			case YAHTZEE: return checkCategory9_10_11_14(category);
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
	
	private int checkCategory9_10_11_14(int category) { // generates points for categories "three of a kind" , "four of a kind" , "full house" and "yahtzee"
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
			else if (numberOfSameDice == 5 && category == YAHTZEE) return 50;
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
	private int[] dice = new int[N_DICE];
}

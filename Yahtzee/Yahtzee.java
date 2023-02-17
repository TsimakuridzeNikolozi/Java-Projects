/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		scores = new int[nPlayers][N_CATEGORIES];
		usedCategories = new boolean[nPlayers][N_CATEGORIES];
		
		int turnsLeft = N_SCORING_CATEGORIES;
		
		for (int i = 0; i < turnsLeft; i++ ) {
			for (int player = 0; player < nPlayers; player++ ) { 
				int[] dice = rolls(player);
				int category = display.waitForPlayerToSelectCategory();
				while (usedCategories[player][category - 1]) { // if the category player chooses is used, make the player to choose a new one
					display.printMessage("Category is used , pick another.");
					category = display.waitForPlayerToSelectCategory();
				}
				
				int score = getScore(player , category , dice); // our score for the roll
				
				// update everything
				display.updateScorecard(category, player+1, score);
				scores[player][category - 1] = score;
				scores[player][TOTAL - 1] += score;
				usedCategories[player][category - 1] = true;
				
				int upperScore = getUpperScore(player) , lowerScore = getLowerScore(player);
				if (upperScore != 0) display.updateScorecard(UPPER_SCORE , player + 1 , upperScore);
				if (lowerScore != 0) display.updateScorecard(LOWER_SCORE, player + 1 , lowerScore);
				display.updateScorecard(TOTAL, player + 1 , scores[player][TOTAL - 1]);
			}
		}
		// displaying winner
		int winner = whoWon();
		display.printMessage("Congratulations, " + playerNames[winner] + ", you're the winner with the total score of " + scores[winner][TOTAL-1] + "!");
	}
	
	private int[] rolls(int index) { // what happens with each roll
		int[] dice = new int[N_DICE];
		for (int i = 0; i < 3; i++) { 
			
			if (i == 0) {
				display.printMessage(playerNames[index] + "'s turn. Click \"Roll Dice\" button to roll the dice.");
				display.waitForPlayerToClickRoll(index+1);
			} else {
				display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\".");
				display.waitForPlayerToSelectDice();
			}
			dice = rollDice(i , dice);
			display.displayDice(dice);
			display.printMessage("Select a category for this roll.");
		}
		return dice;
	}
	
	private int[] rollDice(int turn , int[] dice) { // rolls the dice accordingly
		for (int i = 0; i < N_DICE; i++) {
			if (turn == 0) {
				dice[i] = rgen.nextInt(1 , 6);
			} else {
				if (display.isDieSelected(i)) dice[i] = rgen.nextInt(1 , 6);
			}
		}
		return dice;
	}
	
	private int sumOfDice(int[] dice) { // returns the sum of dice
		int sum = 0;
		for(int i = 0; i < N_DICE; i++) {
			sum += dice[i];
		}
		return sum;
	}
	
	private int getUpperScore(int player) { // sums up the upper scores
		int sum = 0;
		for(int i = ONES ; i < UPPER_SCORE; i++) {
			if (!usedCategories[player][i-1]) return 0;
			sum += scores[player][i-1];
		}
		if (sum >= MINIMAL_SCORE_FOR_UPPER_BONUS) {
			scores[player][UPPER_BONUS - 1] = UPPER_BONUS_POINTS;
			display.updateScorecard(UPPER_BONUS, player + 1 , UPPER_BONUS_POINTS);
			scores[player][TOTAL - 1] += UPPER_BONUS_POINTS;
		}
		return sum;
	}
	
	private int getLowerScore(int player) { // sums up the lower scores
		int sum = 0;
		for(int i = THREE_OF_A_KIND ; i < LOWER_SCORE; i++) {
			if (!usedCategories[player][i-1]) return 0;
			sum += scores[player][i-1];
		}
		return sum;
	}
	
	private int whoWon() { // returns the player who won the game
		int max = 0, player = 0;
		for (int i = 0; i < nPlayers; i++) {
			int playerScore = scores[i][TOTAL-1];
			if (playerScore >= max) {
				max = playerScore;
				player = i;
			}
		}
		return player;
	}
	
	
	
	/*////////////////////////////////////////////////////////
	//if categories are applicable for the given set of dice//
	///////////////////////////////////////////////////////*/
	
	private boolean checkCategories(int[] dice , int category) { // checks if the selected category is applicable for the given set of dice 
		switch(category) {
		case ONES:
		case TWOS:
		case THREES: 
		case FOURS: 
		case FIVES: 
		case SIXES:
		case CHANCE: return true;
		case THREE_OF_A_KIND: 
		case FOUR_OF_A_KIND: return n_Of_A_Kind(category , dice);
		case FULL_HOUSE: return fullHouse(dice);
		case YAHTZEE: return yahtzee(dice);
		case SMALL_STRAIGHT:
		case LARGE_STRAIGHT: return straights(category , dice);
		default: return false;
		}
	}
	
	private int getScore(int player , int category , int[] dice) { // returns a score for the roll
		if (!checkCategories(dice , category)) return 0;
		switch(category) {
		case ONES:
		case TWOS:
		case THREES: 
		case FOURS: 
		case FIVES: 
		case SIXES: return numbers(category , dice);
		case THREE_OF_A_KIND: 
		case FOUR_OF_A_KIND: return sumOfDice(dice);
		case FULL_HOUSE: return 25;
		case YAHTZEE: return 50;
		case SMALL_STRAIGHT: return 30;
		case LARGE_STRAIGHT: return 40;
		case CHANCE: return sumOfDice(dice);
		default: return 0;
		}
	}
	
	private int numbers(int category , int[] dice) { // for the categories involving only numbers(ONES , TWOS, ... , SIXES)
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == category) score += category;
		}
		return score;
	}
	
	private boolean n_Of_A_Kind(int category , int[] dice) { // for the categories THREE_OF_A_KIND and FOUR_OF_A_KIND
		int n = 0;
		
		if (category == THREE_OF_A_KIND) n = 3;
		else n = 4;
		
		for (int i = 0; i < N_DICE; i++) {
			int m = 0;
			for (int j = 0; j < N_DICE; j++) {
				if (dice[i] == dice[j]) m++;
			}
			if (m >= n) return true;
		}
		return false;
	}
	
	private boolean fullHouse(int[] dice) { // for "FULL_HOUSE"
		for (int i = 0; i < N_DICE; i++) {
			int n = 0;
			for (int j = 0; j < N_DICE; j++) {
				if (dice[i] == dice[j]) n++;
			}
			if (n != 2 && n != 3) return false;
		}
		return true;
	}
	
	private boolean yahtzee(int[] dice) { // for "YAHTZEE"
		int example = dice[0];
		for (int i = 1; i < N_DICE; i++) {
			if (dice[i] != example) return false;
		}
		return true;
	}
	
	private boolean straights(int category , int[] dice) { // for categories "SMALL_STRAIGHT" and "LARGE_STRAIGHT"
		Arrays.sort(dice);
		int n = 0;
		for (int i = 1; i < N_DICE; i++) {
			if (dice[i] - dice[i-1] == 1) n++;
			else if (dice[i] == dice[i-1] || i == N_DICE-1) continue;
			else n = 0;
		}
		
		if ( (category == SMALL_STRAIGHT && n >= 3) || (category == LARGE_STRAIGHT && n == 4) ) return true;
		return false;
	}

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	
	private int[][] scores;
	private boolean[][] usedCategories;

}

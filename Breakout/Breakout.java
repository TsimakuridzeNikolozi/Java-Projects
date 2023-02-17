/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

	public class Breakout extends GraphicsProgram {
	
		/** Width and height of application window in pixels */
		public static final int APPLICATION_WIDTH = 400;
		public static final int APPLICATION_HEIGHT = 600;
	
		/** Dimensions of game board (usually the same) */
		private static final int WIDTH = APPLICATION_WIDTH;
		private static final int HEIGHT = APPLICATION_HEIGHT;
	
		/** Dimensions of the paddle */
		private static final int PADDLE_WIDTH = 60;
		private static final int PADDLE_HEIGHT = 10;
	
		/** Offset of the paddle up from the bottom */
		private static final int PADDLE_Y_OFFSET = 70;
	
		/** Number of bricks per row */
		private static final int NBRICKS_PER_ROW = 10;
	
		/** Number of rows of bricks */
		private static final int NBRICK_ROWS = 10;
	
		/** Separation between bricks */
		private static final int BRICK_SEP = 4;
	
		/** Width of a brick */
		private static final int BRICK_WIDTH =
				(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;
	
		/** Height of a brick */
		private static final int BRICK_HEIGHT = 10;
	
		/** Radius of the ball in pixels */
		private static final int BALL_RADIUS = 10;
	
		/** Offset of the top brick row from the top */
		private static final int BRICK_Y_OFFSET = 70;
	
		/** Number of turns */
		private static final int NTURNS = 3;
		
		private RandomGenerator rgen = RandomGenerator.getInstance(); 
		
		private int numBricks = NBRICK_ROWS *  NBRICKS_PER_ROW , turns = NTURNS;
		
		private double vx , vy = 5;
		
		private GRect paddle;
		private GOval ball;

		/* Method: run() */
		/** Runs the Breakout program. */	
			public void init() {
				drawBricks();
				drawPaddle();
				drawBall();
			}
			
			public void run() {
				addMouseListeners();
				
				vx = rgen.nextDouble(1.0, 3.0);
				if (rgen.nextBoolean(0.5)) vx = -vx;
				
				while(true) {
					play();
				}
			}
			
			private void drawBricks() { // adds bricks to the window
				for (int i = 0; i < NBRICK_ROWS; i++) {
					for (int j = 0; j < NBRICKS_PER_ROW; j++) {
						int xCor = (BRICK_SEP + BRICK_WIDTH) * i;
						int yCor = BRICK_Y_OFFSET + (BRICK_SEP + BRICK_HEIGHT) * j;
						
						GRect brick = new GRect(xCor , yCor , BRICK_WIDTH, BRICK_HEIGHT);
						brick.setFilled(true);
						brick.setColor(chooseColor(j));
						add(brick);
					}
				}
			}
			
			private Color chooseColor(int j) { // sets colors for the bricks
				Color brickColor = null;
				if (j < 2) {
					brickColor = Color.RED;
				} else if (j < 4) {
					brickColor = Color.ORANGE;
				} else if (j < 6) {
					brickColor = Color.YELLOW;
				} else if (j < 8) {
					brickColor = Color.GREEN;
				} else if (j < 10) {
					brickColor = Color.CYAN;
				}
				print(brickColor + "   ");
				return brickColor;
			}
			
			private void drawPaddle() { // draws paddle
				int xCor = getWidth()/2 - PADDLE_WIDTH/2;
				int yCor = HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
				paddle = new GRect(xCor , yCor , PADDLE_WIDTH, PADDLE_HEIGHT);
				paddle.setFilled(true);
				add(paddle);
			}
			
			private void drawBall() { // draws ball
				ball = new GOval(getWidth()/2 , getHeight()/2 , BALL_RADIUS * 2 , BALL_RADIUS * 2);
				ball.setFilled(true);
				add(ball);
			}
			
			public void mouseMoved(MouseEvent moved) { // what to do when mouse moves
				if (paddle != null) {
					if (moved.getX() - (PADDLE_WIDTH/2) >= 0 && moved.getX() + (PADDLE_WIDTH/2) <= getWidth()) {
						paddle.setLocation(moved.getX() - (PADDLE_WIDTH/2) , HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
					}
				}
			}
			
			private void play() { // playing process
				ball.move(vx, vy);
				pause(10);
				ifCollidedWithObject();
				ifCollidedWithWalls();
			}
			
			private void ifCollidedWithObject() { // if ball collides with the paddle or bricks
				GObject collider = collidedObject();
				if (collider != null) {
					vy = -vy;
					if (collider != paddle) {
						remove(collider);
						numBricks--;
						if (numBricks == 0) {
							removeAll();
							GLabel won = new GLabel("YOU WIN");
							add(won, getWidth()/2 - won.getWidth()/2 , getHeight()/2 - won.getHeight());
							turns = NTURNS;
							numBricks = NBRICK_ROWS *  NBRICKS_PER_ROW;
							pause(3000);
							startGame();
						}
					}
					while(ball.getY() + 2 * BALL_RADIUS >= paddle.getY() + vy) {
						ball.move(vx , vy);
						pause(10);
					}
				}
			}
			
			private void ifCollidedWithWalls() { // if ball collides with walls
				if (ball.getX() <= 0) {
					vx = -vx;
				} else if (ball.getY() <= 0) {
					vy = -vy;
				} else if (ball.getX()  + 2 * BALL_RADIUS >= getWidth()) {
					vx = -vx;
				} else if (ball.getY() + 2 * BALL_RADIUS >= getHeight()) {
					turns--;
					if (turns == 0) {
						removeAll();
						GLabel lost = new GLabel("LOST");
						add(lost, getWidth()/2 - lost.getWidth()/2 , getHeight()/2 - lost.getHeight());
						turns = NTURNS;
						numBricks = NBRICK_ROWS *  NBRICKS_PER_ROW;
						pause(3000);
					}
					startGame();
				}
			}
			
			private GObject collidedObject() { // returns the object which the ball touches
				if (getElementAt(ball.getX(), ball.getY()) != null) {
					return getElementAt(ball.getX(), ball.getY());
				} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null) {
					return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
				} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != null) {
					return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
				} else if (getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS) != null) {
					return getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
				}
				return null;
			}
			
			private void startGame() { // starting the game again
				removeAll();
				drawBricks();
				drawPaddle();
				drawBall();
				run();
			}
		}

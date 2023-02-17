import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class BreakoutExtension extends GraphicsProgram {

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
	private static final int PADDLE_Y_OFFSET = 30;

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
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

	// instance variables
	private Color ballColor , backgroundColor , paddleColor;
	
	private String objectType;
	
	private RandomGenerator rgen = RandomGenerator.getInstance(); 
	
	private int numberOfBricks, lives;
	
	private double vx , vy = 3;
	
	private GRect paddle;
	private GOval ball;
	
	private GLabel livesRemaining;
	
	private Font myFont = new Font("Serif", Font.BOLD, 20);
	
	private AudioClip backgroundMusic = MediaTools.loadAudioClip("backgroundMusic.au");
	
	private String currentStateOfPaddle = "normal";
	
	
	/* 												DESCRIPTION OF THE EXTENSION
	 *  1) background music
	 * 	2) option of choosing a colour of the ball
	 *  3) option of choosing a background colour
	 *  4) countdown to get ready for the game with specific sound effects
	 *  5) winning and losing sound effects
	 *  6) special feature of either increasing or decreasing the size of a paddle occasionally, which makes the game
	 *     easier or harder
	 */

/** Runs the Breakout program. */	
	public void run() {
		addMouseListeners();
		initialize();
		buildBricks();
		buildPaddle();
	}
	
	private void initialize() { // starting interface of the game and base value of variables
		setBackground(Color.WHITE); 
		GLabel clickToStart = new GLabel("CLICK SOMEWHERE TO START");
		clickToStart.setFont(myFont);
		add(clickToStart, getWidth()/2 - clickToStart.getWidth()/2 , getHeight()/2 - clickToStart.getHeight());
		waitForClick();
		remove(clickToStart);
		backgroundMusic.play();
		ballColor = null;
		backgroundColor = null;
		paddleColor = null;
		objectType = "ball";
		chooseBallColor();
		chooseBackgroundColor();
		choosePaddleColor();
		setBackground(backgroundColor);
		getReady();
		lives = NTURNS;
		numberOfBricks = NBRICK_ROWS *  NBRICKS_PER_ROW;
		livesRemaining = new GLabel("LIVES LEFT: " + lives);
		livesRemaining.setFont(myFont);
		add(livesRemaining , 5 , livesRemaining.getHeight());
	}
	
	private void chooseBallColor() { //choose colour of the ball
		displayColors();
		choosingProcess("BALL");
		if (ballColor == null) {
			ballColor = Color.BLACK;
			objectType = "background";
		}
		removeAll();
	}
	
	private void chooseBackgroundColor() { // choose background colour
		displayBackgroundColors();
		choosingProcess("BACKGROUND");
		if (backgroundColor == null || backgroundColor == Color.BLACK) {
			backgroundColor = Color.WHITE;
			objectType = "paddle";
		}
		removeAll();
	}
	
	private void choosePaddleColor() { //choose colour of the paddle
		displayColors();
		choosingProcess("PADDLE");
		if (paddleColor == null) {
			paddleColor = Color.BLACK;
		}
		removeAll();
	}
	
	private void displayColors() { // display available colour options for the ball or the paddle
		GOval exampleBall;
		double radius = 30 , separationX = (WIDTH - 6 * radius)/4.0 , separationY = (HEIGHT - 4 * radius)/3;
		Color ballColors[] = {Color.BLACK , Color.CYAN , Color.GREEN , Color.YELLOW , Color.orange , Color.RED};
		for (int i = 0; i < 2; i++) { 
			for (int j = 0; j < 3; j++) {
				exampleBall = new GOval(WIDTH - (6 - 2 * j) * radius - (3 - j) * separationX, HEIGHT - (4 - 2 * i) * radius - (2 - i) * separationY, 2 * radius , 2 * radius);
				exampleBall.setFilled(true);
				exampleBall.setColor(ballColors[i * 3 + j]);
				add(exampleBall);
			}
		}
	}
	
	private void displayBackgroundColors() { // display available colour options for background
		GRect exampleRect;
		double side = 60 , separation = (WIDTH - 5 * side)/7.0;
		Color backgroundColors[] = {Color.BLUE , Color.DARK_GRAY , Color.GRAY , Color.LIGHT_GRAY , Color.PINK};
		for (int i = 0; i < 5; i++) {
			exampleRect = new GRect(WIDTH - (5 - i) * side - (6 - i) * separation , HEIGHT/2 - side/2 ,  side , side);
			exampleRect.setFilled(true);
			exampleRect.setColor(backgroundColors[i]);
			add(exampleRect);
		}
		exampleRect = new GRect(WIDTH - 3 * side - 4 * separation , HEIGHT/2 + side ,  side , side);
		exampleRect.setFilled(true);
		exampleRect.setFillColor(Color.WHITE);
		add(exampleRect);
	}
	
	private void choosingProcess(String type) { // controls the colour choosing process
		int secondsLeft = 10;
		GLabel chooseColor;
		Font font = new Font("Serif", Font.BOLD, 15);
		for (int i = 0; i < 10; i++) {
			chooseColor = new GLabel("CHOOSE " + type + " COLOR IN " + secondsLeft + " SECONDS");
			chooseColor.setFont(font);
			add(chooseColor , (WIDTH - chooseColor.getWidth())/2 , chooseColor.getHeight());
			secondsLeft--;
			pause(1000);
			if (type == "BALL") {
				if (ballColor != null) {
					break;
				}
			} else if (type == "BACKGROUND") {
				if (backgroundColor != null) {
					break;
				}
			} else if (type == "PADDLE") {
				if (paddleColor != null) {
					break;
				}
			}
			remove(chooseColor);
		}
	}
	
	public void mouseClicked(MouseEvent clicked) { // what to do when mouse is clicked
		if (getElementAt(clicked.getX() , clicked.getY()) != null && objectType != null) {
			if (getElementAt(clicked.getX() , clicked.getY()).getColor() != Color.WHITE && objectType == "ball") {
				ballColor = getElementAt(clicked.getX() , clicked.getY()).getColor();
				objectType = "background";
			} else if (getElementAt(clicked.getX() , clicked.getY()).getColor() != Color.WHITE && objectType == "background") {
				if (ballColor != getElementAt(clicked.getX() , clicked.getY()).getColor()) {
					backgroundColor = getElementAt(clicked.getX() , clicked.getY()).getColor();
					objectType = "paddle";
				}
			} else if (getElementAt(clicked.getX() , clicked.getY()).getColor() != Color.WHITE && objectType == "paddle") {
				if (backgroundColor != getElementAt(clicked.getX() , clicked.getY()).getColor()) {
					paddleColor = getElementAt(clicked.getX() , clicked.getY()).getColor();
				}
			}
		}
	}
	
	private void buildBricks() { // builds bricks
		GRect brick;
		double separation = (WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP)/2; // separation from the borders
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				brick = new GRect(separation + BRICK_SEP * j + BRICK_WIDTH * j , BRICK_Y_OFFSET + i * (BRICK_HEIGHT + BRICK_SEP), BRICK_WIDTH , BRICK_HEIGHT);
				brick.setFilled(true);
				assignColorToBricks(i , brick);
			}
		}
	}
	
	private void assignColorToBricks(int i , GRect brick) { // assigns colours to individual bricks
		if (i < 2) {
			brick.setColor(Color.RED);
		} else if (i >= 2 && i < 4) {
			brick.setColor(Color.ORANGE);
		} else if (i >= 4 && i < 6) {
			brick.setColor(Color.YELLOW);
		} else if (i >= 6 && i < 8) {
			brick.setColor(Color.GREEN);
		} else {
			brick.setColor(Color.CYAN);
		}
		add(brick);
	}
	
	private void buildPaddle() { // builds the paddle
		double xCor = (WIDTH - PADDLE_WIDTH)/2;
		double yCor = getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(xCor, yCor , PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(paddleColor);
		add(paddle);
		
		buildBall();
	}
	
	public void mouseMoved(MouseEvent moved) { // what to do when mouse moves
		if (paddle != null) {
			if (moved.getX() - (paddle.getWidth()/2) >= 0 && moved.getX() + (paddle.getWidth()/2) <= getWidth()) {
				paddle.setLocation(moved.getX() - (paddle.getWidth()/2) , getHeight() - PADDLE_Y_OFFSET);
			}
		}
	}
	
	private void buildBall() { // builds a ball 
		ball = new GOval(WIDTH/2 - BALL_RADIUS, HEIGHT/2 - BALL_RADIUS, 2 * BALL_RADIUS , 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(ballColor);
		add(ball);
		
		moveBall();
	}
	
	private void moveBall() { // moving actions
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		while(true) {
			ball.move(vx, vy);
			pause(10);
			actionsAfterCollision();
			checkBorders();
		}
	}
	
	private void actionsAfterCollision() { // what to do when a ball collides with the paddle or bricks
		GObject collider = getCollidingObject();
		if (collider != null) {
			if (collider == paddle) {
				if (ball.getY() + 2 * BALL_RADIUS - 3 < paddle.getY()) { // -3 to enter the else if statement
					vy = -vy;
				} else if (ball.getY() + 2 * BALL_RADIUS - 3 >=  paddle.getY()) {
					vx = -vx;
				}
			} else if (collider.getColor() != Color.BLACK) {
				brickCollision(collider);
			}
		}
	}
	
	private GObject getCollidingObject() { // gets the object which collided with the ball
		if (getElementAt(ball.getX(), ball.getY()) != null) {
			return getElementAt(ball.getX(), ball.getY());
		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null) {
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != null) {
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		} else if (getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS) != null) {
			return getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		} else {
			return null;
		}
	}
	
	private void brickCollision(GObject collider) { // what to do when the ball collides with a brick
		remove(collider);
		vy = -vy;
		AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
		bounceClip.play();
		numberOfBricks--;
		if (numberOfBricks == 0) {
			AudioClip winningSound =  MediaTools.loadAudioClip("win.au");
			winningSound.play();
			pause(1000);
			GLabel win = new GLabel("YOU WON");
			remove(ball);
			win.setFont(myFont);
			add(win, WIDTH/2 - win.getWidth()/2 , HEIGHT/2 - win.getHeight());
			startOver();
		}
		specialFeature();
	}
	
	private void checkBorders() { // checks if the ball collided with the borders and performs suitable actions
		if (ball.getX() <= 0) {
			vx = -vx;
		} else if (ball.getY() <= 0 && ball.getX() + 2 * BALL_RADIUS < getWidth()) {
			vy = -vy;
		} else if (ball.getX()  + 2 * BALL_RADIUS >= getWidth() && ball.getY() + 2 * BALL_RADIUS < getHeight()) {
			vx = -vx;
		} else if (ball.getY() + 2 * BALL_RADIUS >= getHeight()) {
			bottomCollision();
		}
	}
	
	private void bottomCollision() { // what to do when the ball collides with the bottom border
		remove(ball);
		lives--;
		remove(livesRemaining);
		livesRemaining = new GLabel("LIVES LEFT: " + lives);
		livesRemaining.setFont(myFont);
		add(livesRemaining , 5 , livesRemaining.getHeight());
		if (lives == 0) {
			backgroundMusic.stop();
			AudioClip losingSound = MediaTools.loadAudioClip("lose.au");
			losingSound.play();
			pause(1000);
			GLabel lose = new GLabel("YOU LOST");
			lose.setFont(myFont);
			add(lose, WIDTH/2 - lose.getWidth()/2 , HEIGHT/2 - lose.getHeight());
			startOver();
		} else {
			getReady();
			buildBall();
		}
	}
	
	private void getReady() { // countdown to get ready for the game
		GLabel countdown , getReady = new GLabel("GET READY");
		getReady.setFont(myFont);
		add(getReady , WIDTH/2 - getReady.getWidth()/2 , HEIGHT/2 - getReady.getHeight()/2);
		AudioClip startSound = MediaTools.loadAudioClip("startSound.au");
		AudioClip countdownSound = MediaTools.loadAudioClip("countdown.au");
		for (int i = 3; i > 0; i--) {
			countdown = new GLabel(Integer.toString(i));
			countdown.setFont(myFont);
			add(countdown , WIDTH/2 , HEIGHT/2 + getReady.getHeight());
			countdownSound.play();
			pause(1000);
			remove(countdown);
		}
		startSound.play();
		remove(getReady);
	}
	
	private void startOver() { // actions after losing or winning
		GLabel clickToContinue = new GLabel("CLICK SOMEWHERE TO CONTINUE");
		clickToContinue.setFont(myFont);
		add(clickToContinue, WIDTH/2 - clickToContinue.getWidth()/2, HEIGHT/2 - clickToContinue.getHeight() + 20);
		waitForClick();
		removeAll();
		run();
	}
	
	private void specialFeature() { // if number of bricks left equals randomly generated number the paddle either increases or decreases in size
		int checkNumber = rgen.nextInt(numberOfBricks - 5, numberOfBricks);
		if (numberOfBricks == checkNumber) {
			if (numberOfBricks % 5 == 0 && currentStateOfPaddle != "normal"){
				paddle.setBounds(paddle.getX() , paddle.getY() , PADDLE_WIDTH , PADDLE_HEIGHT );
				currentStateOfPaddle = "normal";
			} else if (numberOfBricks % 2 == 0 && currentStateOfPaddle != "expanded") {
				paddle.setBounds(paddle.getX() - PADDLE_WIDTH/2 , paddle.getY() , PADDLE_WIDTH * 2 , PADDLE_HEIGHT );
				currentStateOfPaddle = "expanded";
			} else if (numberOfBricks % 2 != 0 && currentStateOfPaddle != "shrinked"){
				paddle.setBounds(paddle.getX() + PADDLE_WIDTH/2 , paddle.getY() , PADDLE_WIDTH / 2 , PADDLE_HEIGHT );
				currentStateOfPaddle = "shrinked";
			}
		}
	}
}

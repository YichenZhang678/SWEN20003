import bagel.*;

import javax.sound.midi.Track;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;


/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2023
 * Please enter your name below
 * @ Yichen Zhang 1299406
 */
public class ShadowDance extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;

    //score title string
    private final static String SCORE_TITLE = "SCORE ";

    //game start title string
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final static String INSTRUCTION_TITLE = "PRESS SPACE TO START\nUSE ARROW KEY TO PLAY";

    //game end message
    private final static String WIN_MODE = "CLEAR";
    private final static String LOSE_MODE = "TRY AGAIN";
    //corresponding score message
    private final static String PERFECT_TIMING = "PERFECT";
    private final static String GOOD_TIMING = "GOOD";
    private final static String BAD_TIMING = "BAD";
    private final static String MISS_TIMING = "MISS";
    private String message ;


    private final Image BACKGROUND_IMAGE = new Image("res/background.png");

    //title output
    private final static int TITLE_SIZE = 64;
    //Font(java.lang.String filename, int size)
    private final Font TITLE_FONT = new Font("res/FSO8BITR.TTF", TITLE_SIZE);
    //The bottom left corner of this message should be located at (220, 250).
    private final static int TITLE_POSITION_X = 220;
    private final static int TITLE_POSITION_Y = 250;

    //instruction line output
    private final static int INSTRUCTION_SIZE = 24;
    private final Font INSTRUCTION_FONT = new Font("res/FSO8BITR.TTF", INSTRUCTION_SIZE);
    //the x-coordinate should be increased by 100 pixels and the y-coordinate by 190 pixels.
    private final static int INSTRUCTION_POSITION_X = TITLE_POSITION_X + 100;
    private final static int INSTRUCTION_POSITION_Y = TITLE_POSITION_Y + 190;

    //Lane output in 4 directions
    private LaneUp laneUp;
    private LaneDown laneDown;
    private LaneLeft laneLeft;
    private LaneRight laneRight;
    private final static int LANE_POSITION_Y = 384;

    //symbol
    private final static int SYMBOL_POSITION_Y = 657;
    private final static int MAX_NOTES = 100;
    private final static int MAX_HOLD_NOTES = 20;

    //target score
    private final static int TARGET_SCORE = 150;
    private final Font RESULT_FONT = new Font("res/FSO8BITR.TTF", TITLE_SIZE);
    //win and lose position are different because when I test my output
    //if they use the same position there will be one not at the middle of the screen
    private final static int RESULT_WIN_POSITION_X = TITLE_POSITION_X + 150;
    private final static int RESULT_LOSE_POSITION_X = TITLE_POSITION_X + 60;
    private final static int RESULT_POSITION_Y = TITLE_POSITION_Y + 150;

    //check if the game has been started and ended
    //all use boolean
    private boolean ifStarted;
    private boolean ifEnded;

    //score information
    //similar method to applying title output
    private int score;
    private int prevScore;
    //font size should be 30
    private final static int SCORE_FONT_SIZE = 30;
    private final Font SCORE_FONT = new Font("res/FSO8BITR.ttf",SCORE_FONT_SIZE);
    //The bottom left corner of this message should be located at (35, 35)
    private final static int SCORE_POSITION_X = 35;
    private final static int SCORE_POSITION_Y = 35;

    //letter information
    //The font size must be set to 40 and the message must be rendered for 30 frames
    private final static int LETTER_FONT_SIZE = 40;
    private final static int LETTER_FRAME = 30;
    private final Font LETTER_FONT = new Font("res/FSO8BITR.ttf", LETTER_FONT_SIZE);
    private int showLetter;

    public ShadowDance() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        //0 means false, 1 means true
        ifStarted = false;
        ifEnded = false;
        //set the initial value of score to zero
        //set the message start to be perfect then change later
        score = 0;
        showLetter = 0;
        message = PERFECT_TIMING;
        readCSV();
    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */
    //some codes copied from the lecture slide(week4 lecture8)
    private void readCSV() {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("res/test1.csv"))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] information = text.split(",");
                //check if the first element of the string is Lane
                if (information[0].equals("Lane")) {
                    //As if method is too messy intellij gives me idea of using switch
                    //for lanes
                    //information[1] is the direction of the notes
                    switch (information[1]) {
                        case "Up":
                            laneUp = new LaneUp(Integer.parseInt(information[2]), LANE_POSITION_Y, SYMBOL_POSITION_Y);
                            break;
                        case "Down":
                            laneDown = new LaneDown(Integer.parseInt(information[2]), LANE_POSITION_Y, SYMBOL_POSITION_Y);
                            break;
                        case "Right":
                            laneRight = new LaneRight(Integer.parseInt(information[2]), LANE_POSITION_Y, SYMBOL_POSITION_Y);
                            break;
                        case "Left":
                            laneLeft = new LaneLeft(Integer.parseInt(information[2]), LANE_POSITION_Y, SYMBOL_POSITION_Y);
                            break;
                    }//for notes and holdNotes
                } else if (information[0].equals("Left")) {
                    if (information[1].equals("Normal")) {
                        laneLeft.noteInfo(new Note(Integer.parseInt(information[2])));
                    } else {
                        laneLeft.holdNoteInfo(new HoldNote(Integer.parseInt(information[2])));
                    }
                } else if (information[0].equals("Right")) {
                    if (information[1].equals("Normal")) {
                        laneRight.noteInfo(new Note(Integer.parseInt(information[2])));
                    } else {
                        laneRight.holdNoteInfo(new HoldNote(Integer.parseInt(information[2])));
                    }
                } else if (information[0].equals("Up")) {
                    if (information[1].equals("Normal")) {
                        laneUp.noteInfo(new Note(Integer.parseInt(information[2])));
                    } else {
                        laneUp.holdNoteInfo(new HoldNote(Integer.parseInt(information[2])));
                    }
                } else if (information[0].equals("Down")) {
                    if (information[1].equals("Normal")) {
                        laneDown.noteInfo(new Note(Integer.parseInt(information[2])));
                    } else {
                        laneDown.holdNoteInfo(new HoldNote(Integer.parseInt(information[2])));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */

    //apply 120HZ
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        //if ended print of win or lose
        //must all the note and holdNote are equal to counts
        ifEnded = laneLeft.ifEnd() && laneRight.ifEnd() && laneDown.ifEnd() && laneUp.ifEnd();
        if (ifEnded) {
            score = laneLeft.getScore() + laneRight.getScore() + laneDown.getScore() + laneUp.getScore();
            if (score >= TARGET_SCORE) {
                //similar to title font use drawString method from bagel
                RESULT_FONT.drawString(WIN_MODE, RESULT_WIN_POSITION_X, RESULT_POSITION_Y);
            } else {
                RESULT_FONT.drawString(LOSE_MODE, RESULT_LOSE_POSITION_X, RESULT_POSITION_Y);
            }
        } else if (!ifStarted) {
            //drawString method from bagel(Font)
            //output the title
            TITLE_FONT.drawString(GAME_TITLE, TITLE_POSITION_X, TITLE_POSITION_Y);

            //output the instruction
            INSTRUCTION_FONT.drawString(INSTRUCTION_TITLE, INSTRUCTION_POSITION_X, INSTRUCTION_POSITION_Y);
            //method from bagel input to check if space is pressed
            //if it is pressed it means that the game has started
            if (input.wasPressed(Keys.SPACE)) {
                ifStarted = true;
            }
        } else {
            //output the score title
            SCORE_FONT.drawString(SCORE_TITLE.concat(Integer.toString(score)), SCORE_POSITION_X, SCORE_POSITION_Y);

            if (showLetter > 0) {
                //find the middle of the screen
                LETTER_FONT.drawString(message, WINDOW_WIDTH / 2 - LETTER_FONT.getWidth(message) / 2, WINDOW_HEIGHT / 2 - LETTER_FONT_SIZE / 2);
                showLetter -= 1;
            }

            //draw lanes
            laneUp.paintCoordinator();
            laneDown.paintCoordinator();
            laneLeft.paintCoordinator();
            laneRight.paintCoordinator();
            //draw Notes and Hold Notes
            laneUp.paintNoteAndHoldNote();
            laneDown.paintNoteAndHoldNote();
            laneLeft.paintNoteAndHoldNote();
            laneRight.paintNoteAndHoldNote();
            //method from bagel check what button we are using
            //for notes
            //for holdNotes
            //Hint: To check if a hold has been released
            //you will find the wasReleased() method in the Input class helpful.
            if (input.wasPressed(Keys.LEFT)) {
                laneLeft.determined();
            } else if (input.wasPressed(Keys.RIGHT)) {
                laneRight.determined();
            } else if (input.wasPressed(Keys.UP)) {
                laneUp.determined();
            } else if (input.wasPressed(Keys.DOWN)) {
                laneDown.determined();
            } else if (input.wasReleased(Keys.LEFT)) {
                laneLeft.released();
            } else if (input.wasReleased(Keys.RIGHT)) {
                laneRight.released();
            } else if (input.wasReleased(Keys.UP)) {
                laneUp.released();
            } else if (input.wasReleased(Keys.DOWN)) {
                laneDown.released();
            }
            score = laneLeft.getScore() + laneRight.getScore() + laneDown.getScore() + laneUp.getScore();
            prevScore = laneLeft.getPrevScore() + laneRight.getPrevScore() + laneDown.getPrevScore() + laneUp.getPrevScore();
            //for printing out what timing we are at
            if (score != prevScore) {
                int DIFF_SCORE = score - prevScore;
                if (DIFF_SCORE == LaneLeft.PERFECT_SCORE) {
                    message = PERFECT_TIMING;
                    showLetter = LETTER_FRAME;
                } else if (DIFF_SCORE == laneLeft.GOOD_SCORE) {
                    message = GOOD_TIMING;
                    showLetter = LETTER_FRAME;
                } else if (DIFF_SCORE == laneLeft.BAD_SCORE) {
                    message = BAD_TIMING;
                    showLetter = LETTER_FRAME;
                } else if (DIFF_SCORE == laneLeft.MISS_SCORE) {
                    message = MISS_TIMING;
                    showLetter = LETTER_FRAME;
                }
            }
        }
    }
}


import bagel.Image;
import bagel.util.Point;

import java.awt.image.BandCombineOp;

public class  LaneDown {
    private final static int WINDOW_HEIGHT = 768;
    private final static int MAX_NOTES = 100;
    private final static int MAX_HOLD_NOTES = 20;
    private final Image DOWN_IMAGE = new Image("res/LaneDown.png");
    private final Image DOWN_NOTE_IMAGE = new Image("res/noteDown.png");
    private final Image DOWN_HOLD_NOTE_IMAGE = new Image("res/holdNoteDown.png");
    //Point method from bagel
    private final Point coordinator;
    private final Point symbol;
    //X and Y
    private int LANE_POSITION_X;
    private int LANE_POSITION_Y;

    //Y symbol
    private int SYMBOL_POSITION_Y;

    //Note array
    private Note[] notes;
    //MAX_NOTES = 100 record how many notes there are
    private int countNotes;

    //Hold-Notes array
    private HoldNote[] holdNotes;
    // MAX_HOLD_NOTES = 20; record how many notes there are
    private int countHoldNotes;

    //when the note is falling position is changing
    //this variable is used to show the current position of note and holdNotes
    private int notePosition;
    private int holdNotePosition;

    //frameNum
    private int refreshNumber;

    //score
    private int score;
    private int prevScore;

    //to see this the test is ended
    private boolean isEnd;

    //X value should be added in Shadow dance class
    //Use this. to give the X and Y value to the coordinator
    public LaneDown(int LANE_POSITION_X, int LANE_POSITION_Y, int SYMBOL_POSITION_Y) {
        this.coordinator = new Point(LANE_POSITION_X, LANE_POSITION_Y);
        this.symbol = new Point(LANE_POSITION_X, SYMBOL_POSITION_Y);
        // arrays of note and holdNote
        this.notes = new Note[MAX_NOTES];
        this.holdNotes = new HoldNote[MAX_HOLD_NOTES];
        //set initial variable to zero
        this.countNotes = 0;
        this.countHoldNotes = 0;
        this.refreshNumber = 0;
        this.score = 0;
        this.notePosition = 0;
        this.holdNotePosition = 0;
        this.prevScore = 0;
    }

    // Getter method for the x-coordinate
    public int getLaneCoordinatorX() {
        return (int) coordinator.x;
    }

    // Getter method for the y-coordinate
    public int getLaneCoordinatorY() {
        return (int) coordinator.y;
    }

    //get symbol x
    public int getSymbolCoordinatorX() {
        return (int) symbol.x;
    }

    //get symbol y
    public int getSymbolCoordinatorY() {
        return (int) symbol.y;
    }

    public int getPrevScore(){
        return prevScore;
    }

    //draw the image
    //draw method from Image in bagel
    public void paintCoordinator() {
        DOWN_IMAGE.draw(getLaneCoordinatorX(), getLaneCoordinatorY());
    }

    //distance and score correspond
    //The method to determine the score from this distance
    //If distance <= 15, this is a PERFECT score and receives 10 points
    private static int PERFECT_DIS = 15;
    public static int PERFECT_SCORE = 10;
    //If 15 < distance <= 50, this is a GOOD score and receives 5 points
    private static int GOOD_DIS = 50;
    public int GOOD_SCORE = 5;
    //If 50 < distance <= 100, this is a BAD score and receives -1 points
    private int BAD_DIS = 100;
    public int BAD_SCORE = -1;
    //If 100 < distance <= 200, this is a MISS and receives -5 points.
    //If the note leaves the window from the bottom of the screen without the corresponding key being pressed
    //this is considered as a MISS too and receives -5 points.
    private int MISS_DIS = 200;
    public int MISS_SCORE = -5;

    // use Google and gpt as some idea on array making
    public void noteInfo(Note note) {
        notes[countNotes] = note;
        countNotes++;
    }

    public void holdNoteInfo(HoldNote holdNote) {
        holdNotes[countHoldNotes] = holdNote;
        countHoldNotes++;
    }

    //paint the note and the hold note
    public void paintNoteAndHoldNote() {
        prevScore = score;
        for (int i = 0; i < countNotes; i++) {
            notes[i].animation(refreshNumber, DOWN_NOTE_IMAGE, coordinator.x);
        }

        for (int i = 0; i < countHoldNotes; i++) {
            holdNotes[i].holdNoteAnimation(refreshNumber, DOWN_HOLD_NOTE_IMAGE, coordinator.x);
        }

        //for note beyond the height
        if (countNotes > 0 && notePosition < countNotes) {
            if (notes[notePosition].getYCoordinate() > WINDOW_HEIGHT) {
                score += MISS_SCORE;
                notePosition++;
            }
        }
        //for holdNote beyond the height
        if (countHoldNotes > 0 && holdNotePosition < countHoldNotes) {
            if (holdNotes[holdNotePosition].getYEnding() > WINDOW_HEIGHT) {
                score += MISS_SCORE;
                holdNotePosition++;
            }
        }
        refreshNumber++;
    }

    //getter function for symbol
    public Point getSymbol() {
        return symbol;
    }

    //getter function for score
    public int getScore() {
        return score;
    }

    //getter function for note position
    public int getNotePosition() {
        return notePosition;
    }

    //getter function fot holdNote position
    public int getHoldNotePosition() {
        return holdNotePosition;
    }

    public void recordScore(){
        prevScore = score;
    }

    //determine the timing we press the button and the score
    public void determined() {
        //for note
        //must have at least one note
        if ((countNotes > 0) && (notePosition < countNotes)) {
            // calculate the distance between the symbol coordinator and the symbol note
            double distance = Math.abs(notes[notePosition].getYCoordinate() - getSymbolCoordinatorY());
            //when equal to perfect distance
            if (distance <= PERFECT_DIS) {
                //score adding
                score += PERFECT_SCORE;
                notes[notePosition].free();
                notePosition++;
                //when equal to good distance
            } else if (distance <= GOOD_DIS && distance > PERFECT_DIS) {
                //score adding
                score += GOOD_SCORE;
                notes[notePosition].free();
                notePosition++;
                //when equal to bad distance
            } else if (distance <= BAD_DIS && distance > GOOD_DIS) {
                //score adding
                score += BAD_SCORE;
                notes[notePosition].free();
                notePosition++;
                //when equal to miss distance
            } else if (distance <= MISS_DIS && distance > BAD_DIS) {
                //score adding
                score += MISS_SCORE;
                notes[notePosition].free();
                notePosition++;
            }else{
                score += MISS_SCORE;
            }
        }

        //for hold note press at first
        //holdNotePosition < countHoldNotes means not until the end
        if ((holdNotePosition < countHoldNotes)) {
            if(countHoldNotes > 0){
                // calculate the distance between the symbol coordinator and the symbol note
                double distance = Math.abs(holdNotes[holdNotePosition].getYBeginning() - getSymbolCoordinatorY());
                //when equal to perfect distance
                if (distance <= PERFECT_DIS) {
                    //score adding
                    score += PERFECT_SCORE;
                    holdNotes[holdNotePosition].free();
                    //when equal to good distance
                } else if (distance <= GOOD_DIS && distance > PERFECT_DIS) {
                    //score adding
                    score += GOOD_SCORE;
                    holdNotes[holdNotePosition].free();
                    //when equal to bad distance
                } else if (distance <= BAD_DIS && distance > GOOD_DIS) {
                    //score adding
                    score += BAD_SCORE;
                    holdNotes[holdNotePosition].free();
                    //when equal to miss distance
                } else if (distance > BAD_DIS && distance <= MISS_DIS) {
                    //score adding
                    score += MISS_SCORE;
                    holdNotes[holdNotePosition].free();
                }
            }
        }
    }

    //for hold notes release at the end
    public void released() {
        //avoid NULL and cause game crashing
        if (countHoldNotes > 0 && holdNotePosition < countHoldNotes) {
            if (holdNotes[holdNotePosition].pressed()) {
                double distance = Math.abs(holdNotes[holdNotePosition].getYEnding() - getSymbolCoordinatorY());
                if (distance <= PERFECT_DIS) {
                    //score adding
                    score += PERFECT_SCORE;
                    holdNotes[holdNotePosition].free();
                    holdNotePosition++;
                    //when equal to good distance
                } else if (distance <= GOOD_DIS && distance > PERFECT_DIS) {
                    //score adding
                    score += GOOD_SCORE;
                    holdNotes[holdNotePosition].free();
                    holdNotePosition++;
                    //when equal to bad distance
                } else if (distance <= BAD_DIS && distance > GOOD_DIS) {
                    //score adding
                    score += BAD_SCORE;
                    holdNotes[holdNotePosition].free();
                    holdNotePosition++;
                    //when equal to miss distance
                } else if (distance > BAD_DIS) {
                    //score adding
                    score += MISS_SCORE;
                    holdNotes[holdNotePosition].free();
                    holdNotePosition++;
                }
            }
        }
    }



    //check if it is the end of each test
    //only if the num of notes and the holdNotes are equal to their counts that means the test is over
    public boolean ifEnd() {
        return ((holdNotePosition == countHoldNotes) && (notePosition == countNotes));
    }
}
import bagel.Image;

public class HoldNote {
    //similar to Note just some difference in hold note animation
    public int refreshNumber;
    //the x-coordinate is the same as the x-coordinate
    //y-coordinate is 24
    private final static int Y_INITIAL = 24;
    //for 120 hz，speed of 2 pixels per frame
    //The speed is the same as for a normal note
    private final static int SPEED = 4;
    //As falling yCoordinate is changing
    //two scores are calculated - first when the hold is started and the second when the hold is released
    private int yCoordinate;
    private double xCoordinate;
    //Hint: To calculate the y-coordinate at the bottom of the hold note image
    //add 82 to the centre y-coordinate, and for the y-coordinate at the top of the image, subtract 82.
    private final static int DISTANCE_START_END = 82;

    //if the frame is at that point
    private int timing;

    //if we pressed that button
    private boolean wasPressed;

    public HoldNote(int refreshNumber) {
        this.refreshNumber = refreshNumber;
        this.timing = 0;
        this.wasPressed = false;
        yCoordinate  = Y_INITIAL;
        //this.wasPressed = 0;
    }

    //for hold note falling animation
    public void holdNoteAnimation(int refreshNumber, Image image, double xCoordinate) {
        //when they are the same, that means it is the correct timing for paint the image
        if (refreshNumber == this.refreshNumber) {
            timing = 1;
        }
        //to avoid game crashing, if（！wasPressed） must be added
        if (timing == 1) {
            //draw the point
            image.draw(xCoordinate, yCoordinate);
            // add the speed for 120hz
            yCoordinate += SPEED;
        }
    }

    public boolean pressed(){
        return wasPressed;
    }
    public void released(){
        timing = 0;
    }

    //Hint: To calculate the y-coordinate at the bottom of the hold note image
    //add 82 to the centre y-coordinate, and for the y-coordinate at the top of the image, subtract 82.
    //getter function for yBeginning
    public int getYBeginning() {
        return yCoordinate + DISTANCE_START_END;
    }

    //getter function for yEnding(){
    public int getYEnding() {
        return yCoordinate - DISTANCE_START_END;
    }

    //getter function for xCoo
    public double getXCoordinate() {
        return xCoordinate;
    }

    //getter function for press

    public boolean getWasPressed() {
        return wasPressed;
    }

    public void free(){
        wasPressed = true;
    }
}

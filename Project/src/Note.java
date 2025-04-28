import bagel.Image;

public class Note {
    private int refreshNumber;
    //y-coordinate is 100
    private final static int Y_INITIAL= 100;
    //for 120 hz，speed of 2 pixels per frame
    private final static int SPEED = 4;
    //As falling yCoordinate is changing
    private int yCoordinate;
    private double xCoordinate;

    //if the frame is at that point
    private boolean timing;

    //if we pressed that button
    private boolean wasPressed;
    public Note(int refreshNumber){
        this.refreshNumber = refreshNumber;
        this.yCoordinate = Y_INITIAL;
        //0 means false, 1 means true
        this.timing = false;
        this.wasPressed = false;
    }

    //for falling note animation
    public void animation(int refreshNumber, Image image, double xCoordinate) {
        //when they are the same, that means it is the correct timing for paint the image
        if (refreshNumber == this.refreshNumber) {
            timing = true;
        }
        //to avoid game crashing, if（！wasPressed） must be added
        if (!wasPressed ) {
            if (timing) {
                //draw the point
                image.draw(xCoordinate, yCoordinate);
                yCoordinate += SPEED;
            }
        }
    }

    //getter function for yCoo
    public double getYCoordinate(){
        return yCoordinate;
    }


    //for press function what we want is when a note is pressed we want it disappear
    public void free(){
        wasPressed = true;
    }
}

